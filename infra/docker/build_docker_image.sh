#!/usr/bin/env bash

set -e
trap 'echo "An error occurred. Exiting..."; exit 1;' ERR

# --- Functions ---
## Check if a variable is empty
function check_var() {
    if [ -z "$1" ]; then
        echo "Not allowed empty input"
        exit 1
    fi
}

## Prompt the user for input until a valid response is provided
function prompt_until_valid() {
    local prompt_message=$1
    local var_name=$2
    while true; do
        read -r -p "${prompt_message}: " value
        if [ -n "${value}" ]; then
            eval "${var_name}='${value}'"
            break
        else
            echo "Input cannot be empty. Please try again."
        fi
    done
}

## Validate if a number is entered
function validate_number() {
    local number=$1
    local message=$2
    if ! [[ "$number" =~ ^[0-9]+$ ]]; then
        echo "$message"
        exit 1
    fi
}

## Validate if a directory exists
function validate_directory() {
    local dir=$1
    if [ ! -d "$dir" ]; then
        echo "Error: Directory '$dir' does not exist."
        exit 1
    fi
}


# --- Main Script ---
## Prompt for image tag
prompt_until_valid "Enter the tag of docker image (e.g. centos/dev_$(id -un):8.4.2105)" image_tag

## Prompt for Dockerfile directory and validate
prompt_until_valid "Enter the directory path where the Dockerfile is located" docker_dir
validate_directory "$docker_dir"

## Check if the Docker image already exists
if docker image inspect "${image_tag}" &>/dev/null; then
    echo "Error: The Docker image '${image_tag}' already exists."
    echo "Please try again with another image name."
    exit 1
fi

## Ask if the image is for personal use
FOR_PERSONAL=true
while true; do
    read -r -p "Do you use it for personal purposes? (y, n)(q: quit task): " use_personal
    case $use_personal in
        [yY]) FOR_PERSONAL=true; break ;;
        [nN]) FOR_PERSONAL=false; break ;;
        [qQ]) echo "Canceled Docker image creation."; exit 1 ;;
        *) echo "Invalid input. Please enter 'y', 'n', or 'q'." ;;
    esac
done


## Collect user information
if $FOR_PERSONAL; then
    username=$(id -un)
    userid=$(id -u)
    groupname=$(id -gn)
    groupid=$(id -g)
else
    prompt_until_valid "Enter user name" username
    prompt_until_valid "Enter user ID" userid
    validate_number "$userid" "Error: User ID must be a number."
    prompt_until_valid "Enter group name" groupname
    prompt_until_valid "Enter group ID" groupid
    validate_number "$groupid" "Error: Group ID must be a number."
fi


## Build the Docker image
echo "Building Docker image '${image_tag}'..."
docker build -t "$image_tag" \
    --build-arg username="$username" \
    --build-arg userid="$userid" \
    --build-arg groupname="$groupname" \
    --build-arg groupid="$groupid" \
    "$docker_dir"

echo "Docker image '${image_tag}' built successfully!"