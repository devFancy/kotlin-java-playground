# Docker Scripts for CentOS and Ubuntu

This project provides Docker scripts for building and running Docker images tailored for CentOS and Ubuntu environments.

## Directory Structure

```plaintext
docker/
├── build_docker_image.sh       # Script to build Docker images
├── run_docker_container.sh     # Script to create and run Docker containers
├── centos
│   └── 8.4.2105
│       └── Dockerfile          # Dockerfile for CentOS 8.4.2105
└── ubuntu
    └── 20.04
        └── Dockerfile          # Dockerfile for Ubuntu 20.04
```

# Prerequisites

* Docker installed and running on the system.

* Sufficient permissions to run Docker commands (sudo or being part of the docker group).

* Ensure the host port specified is available.


# Usage

## Build Docker Images

Use the `build_docker_image.sh` script to build Docker images for CentOS or Ubuntu environments.

### Command

```bash
./build_docker_image.sh
```

### Example (CentOS)

```bash
Enter the tag of docker image (e.g., centos/dev_{username}:8.4.2105): centos/devfancy:8.4
Enter the directory path where the Dockerfile is located: ./centos/8.4.2105/
Do you use it for personal purposes? (y, n)(q: quit task): y


## Output:
Docker image 'centos/devfancy:8.4' built successfully!
```

### Example (Ubuntu)

```bash
Enter the tag of docker image (e.g., ubuntu/dev_{username}:20.04): ubuntu/devfancy:20.04
Enter the directory path where the Dockerfile is located: ./ubuntu/20.04/
Do you use it for personal purposes? (y, n)(q: quit task): y


## Output:
Docker image 'ubuntu/devfancy:20.04' built successfully!
```

## Run Docker Containers

Use the `run_docker_container.sh` script to create and run containers from built Docker images.

### Command

```bash
./run_docker_container.sh
```

### Example (CentOS)

```bash
Enter the docker image: centos/devfancy:8.4
Enter the container name: centos-container
Enter the host port to map to container (default: 8081): 8081


## Output:
Port 8081 is available.
Project root path: /path/to/{current_folder_name}
Creating container: centos_container
Mount point: host:/home/jymoon/test -> container:/app
{DOCKER_CONTAINER_ID}
Container 'centos_container' is running.
Accessing container shell...
To manually access the container later, run:
  docker exec -it centos_container /bin/bash
```

### Example (Ubuntu)

```bash
Enter the docker image: ubuntu/devfancy:20.04
Enter the container name: ubuntu_container
Enter the host port to map to container (default: 8081): 8082


## Output:
Port 8082 is available.
Project root path: /path/to/{current_folder_name}
Creating container: ubuntu_container
Mount point: host:/home/jymoon/test -> container:/app
{DOCKER_CONTAINER_ID}
Container 'ubuntu_container' is running.
Accessing container shell...
To manually access the container later, run:
  docker exec -it ubuntu_container /bin/bash
To run a command as administrator (user "root"), use "sudo <command>".
See "man sudo_root" for details.
```