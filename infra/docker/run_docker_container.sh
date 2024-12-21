#!/usr/bin/env bash

set -e
trap 'echo "An error occurred. Exiting..."; exit 1;' ERR

CUR_DIR="$(cd "$(dirname "$0")" && pwd -P)"

# Function to validate input
function check_var() {
    if [[ -z "$1" ]]; then
        echo "Error: $2 cannot be empty."
        exit 1
    fi
}

# Function to check if a port is available
function check_port() {
    local port=$1
    if netstat -tuln | grep -q ":${port}"; then
        return 1  # Port is in use
    fi
    return 0  # Port is available
}

# Prompt for a valid port - Loop to ensure the entered port is available
function prompt_for_port() {
    while true; do
        read -r -p "Enter the host port to map to container (default: 8081): " host_port
        host_port=${host_port:-8081}

        if check_port "${host_port}"; then
            echo "Port ${host_port} is available."
            break
        else
            echo "Error: Port ${host_port} is already in use. Please enter another port."
        fi
    done
}

# Prompt for image and container names
read -r -e -p "Enter the docker image: " image_name
check_var "${image_name}" "Docker image name"

read -r -p "Enter the container name: " container_name
check_var "${container_name}" "Container name"

echo "Docker Image: ${image_name}, Container Name: ${container_name}"

# Check if the Docker image exists
if ! docker image inspect "${image_name}" &>/dev/null; then
    echo "Error: Docker image '${image_name}' does not exist."
    exit 1
fi

# Check if the container name is already in use
if docker ps -a --format '{{.Names}}' | grep -w -q "${container_name}"; then
    echo "Error: Container name '${container_name}' is already in use."
    exit 1
fi

USER=$(id -un)
ROOT_DIR=$(dirname "${CUR_DIR}")

# Loop to ensure the entered port is available
prompt_for_port

echo "Project root path: ${ROOT_DIR}"
echo "Creating container: ${container_name}"
echo "Mount point: host:${ROOT_DIR} -> container:/app"

# Create and start the container without GPU options
docker run -itd -v "${ROOT_DIR}:/app" -p "${host_port}:8081" --name "${container_name}" "${image_name}"

if ! docker ps --filter "name=${container_name}" --format '{{.Names}}' | grep -w -q "${container_name}"; then
    echo "Error: Failed to start the container '${container_name}'."
    exit 1
fi

echo "Container '${container_name}' is running."
echo "Accessing container shell..."
echo "To manually access the container later, run:"
echo "  docker exec -it ${container_name} /bin/bash"

docker exec -it -u "$(id -u)" "${container_name}" /bin/bash || \
docker exec -it "${container_name}" /bin/bash