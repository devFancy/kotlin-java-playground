# Dockerfile 간단히 알아보기: 명령어와 실습 예제

작성한 최초 날짜: 2024.12.21

---

## Dockerfile 이란

* `Dockerfile`은 Docker 이미지를 생성하기 위해 사용되는 텍스트 기반의 설정 파일이다.

    * `Dockerfile`에 명령어를 작성함으로써 애플리케이션이 어떤 환경에서 실행되고, 필요한 의존성을 어떻게 설치하며, 실행 시 어떤 설정이 필요한지를 정의할 수 있다.

    * `Dockerfile` 기반으로 docker build 명령어를 실행하면 해당 설정에 따라 새로운 Docker 이미지를 생성한다.

---

## 주요 명령어 살펴보기

1. 이미지 기본 설정: `FROM`, `LABEL`, `ARG`

2. 환경 설정: `ENV`, `USER`, `WORKDIR`

3. 작업 및 파일 처리: `RUN`

4. 파일 복사: `COPY`, `ADD`

### FROM

* 기본 이미지를 지정하며, 이 명령어는 Docker 이미지의 기반을 설정한다. 모든 Dockerfile은 최소한 하나의 FROM 명령어를 포함해야 한다.

```docker
FROM ubuntu:20.04
```

### LABEL

* Docker 이미지에 메타데이터를 추가하기 위한 명령어입니다. 이 메타데이터는 이미지 설명, 작성자 정보, 버전 정보 등을 기록하는 데 사용됩니다.

    * 예를 들어, 작성자, 이메일, 이미지 설명 등을 정의할 수 있습니다.

* 키와 값은 키-값 쌍으로 작성해야 하며, 쉼표(,) 대신 공백을 사용한다.

```docker
LABEL authors="devfancy" email="fancy.juyongmoon@gmail.com"
```

### ARG

* **빌드 시점에 전달되는 변수를 정의**한다. 해당 변수들은 빌드 중에 값을 지정할 수 있으며, 기본값도 설정 가능하다. ARG는 빌드시점에만 유효하고 런타임에 접근할 수 없다.

    * 0: root(슈퍼 유저), 1 ~ 999: 시스템 계정, 1000 이상: 일반 사용자 계정

```docker
ARG username=user
ARG userid=1000
ARG groupname=user
ARG groupid=1000
```

### ENV

* **빌드 시 설정할 환경 변수를 정의**한다. 런타임 동안 지속적으로 사용할 값을 설정할 수도 있다.

```docker
ENV DEBIAN_FRONTEND=noninteractive
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV PATH=$PATH:${JAVA_HOME}/bin
```

### USER

* 컨테이너 실행시 사용할 기본 사용자를 설정한다. (보안을 위해 루트 대신 제한된 권한의 사용자 계정을 사용하는 것이 일반적임)

```docker
USER $username:$groupname
```

### WORKDIR

* 컨테이너 내에서 명령어가 실행될 기본 작업 디렉터리를 설정한다. **절대 경로**를 사용하는 것을 권장하며, 이는 컨테이너의 디렉터리 구조와 작업 환경을 명확히 설정한다.

```docker
WORKDIR /app
```

### RUN

* Docker 이미지 빌드 중 실행할 명령어를 정의한다. 여러 RUN 명령을 사용하면 디버깅이 쉬워지지만, 레이어 수가 증가할 수 있으므로 적절히 병합할 것을 권장한다.

```docker
# 패키지 업데이트 및 설치
RUN apt-get update
RUN apt-get -y upgrade
RUN apt-get -y install sudo debianutils wget gzip openjdk-11-jdk
RUN apt-get clean

# 사용자 및 그룹 생성
RUN getent group $groupname || groupadd -g $groupid $groupname
RUN useradd -u $userid -g $groupname -m $username -s /bin/bash
RUN usermod -aG sudo $username
RUN echo "$username ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers.d/$username


# 작업 디렉터리 생성 및 소유권 설정
RUN if [ ! -d "/app" ]; then mkdir /app; else echo "/app already exists"; fi
RUN chown $username:$groupname -R /app
```

### COPY

* 로컬의 파일이나 디렉터리를 컨테이너 이미지 내로 복사한다.

```docker
COPY /path/to/my_app /app
```

### ADD

* `COPY`와 유사하지만, URL에서 파일 다운로드 및 압축 해제 기능도 제공한다. `ADD`는 URL에서 파일을 다운로드하거나 압축 파일을 자동으로 해제할 때 사용하지만, 일반적으로는 더 단순한 `COPY`를 권장한다.

```docker
ADD https://example.com/my_app.tar.gz /app/
```

## Dockerfile로 간단한 애플리케이션 빌드하기

> 아래 과정을 수행하기 위해서는 docker 가 설치되어 있어야 한다. (확인하는 명령어: docker --version)

* 아래는 Docker 이미지를 Ubuntu를 기반으로 빌드하는 `Dockerfile`이다.

```dockerfile
# Ubuntu 20.04
FROM ubuntu:20.04

# 메타데이터 추가
LABEL authors="devfancy"
LABEL email="fancy.juyongmoon@gmail.com"

# 비대화형 프롬프트 비활성화
ENV DEBIAN_FRONTEND=noninteractive

# 빌드 시점 변수 설정
ARG username=user
ARG userid=1000
ARG groupname=user
ARG groupid=1000

# 필수 패키지 업데이트 및 설치
USER root
RUN apt-get update && apt-get -y upgrade
RUN apt-get -y install \
    sudo \
    debianutils \
    wget \
    gzip \
    openjdk-11-jdk
RUN apt-get clean

# Java 환경 변수 설정
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64
ENV PATH $PATH:${JAVA_HOME}/bin

# 사용자 및 그룹 생성
RUN getent group $groupname || groupadd -g $groupid $groupname
RUN useradd -u $userid -g $groupname -m $username -s /bin/bash
RUN usermod -aG sudo $username
RUN echo "$username ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers.d/$username

# 작업 디렉토리 생성 및 소유권 설정
RUN if [ ! -d "/app" ]; then mkdir /app; else echo "/app already exists"; fi && \
    chown $username:$groupname -R /app

# 사용자 변경 (보안을 위해 루트 대신 사용자 계정으로 실행)
USER $username:$groupname

# 작업 디렉토리 설정
WORKDIR /app
```

> 추가 설명

* `echo "$username ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers.d/$username`

    * sudo 권한을 부여하기 위한 설정

    * 이 명령은 새로운 사용자 $username이 sudo를 사용할 때 비밀번호를 요구하지 않도록 설정한다.

        * `ALL=(ALL)`: 이 사용자는 모든 사용자로 전환할 수 있음

        * `NOPASSWD: ALL`: sudo 명령 실행시 비밀번호를 요구하지 않음

        * `/etc/sudoers.d/$username`: 새로운 사용자에 대한 sudo 규칙을 저장하는 파일

---

## Docker 빌드 명령 실행

* 해당 도커파일이 있는 위치로 이동한다. (위치: /path/to/ubuntu, ubuntu 폴더 안에 Dockerfile이 있다는 가정)

* 이미지 빌드

    * Docker 이미지를 빌드하려면 아래 명령어를 입력합니다. 이 명령어는 현재 시스템의 사용자 계정 정보를 자동으로 사용한다.

  ```bash
  docker build -t ubuntu:20.04 \
  --build-arg username=$(id -un) \
  --build-arg userid=$(id -u) \
  --build-arg groupname=$(id -gn) \
  --build-arg groupid=$(id -g) \
  . # Dockerfile이 현재 디렉터리에 있다고 가정, 다른 경로에 있다면 절대 경로나 상대 경로를 입력해야 함
  ```


* Docker는 지정된 `Dockerfile`과 `--build-arg`로 전달된 값을 사용하여 이미지를 빌드합니다. 빌드가 완료되면 `ubuntu:20.04` 이라는 이름으로 저장된다.

    * `$(id -un)`: 현재 사용자 이름

    * `$(id -u)`: 현재 사용자 ID

    * `$(id -gn)`: 현재 사용자 그룹 이름

    * `$(id -g)`: 현재 사용자 그룹 ID



* 빌드된 이미지를 확인하기 위해 다음과 같은 명령어를 입력한다.

```bash
$ docker images
```

> 출력 예시

```plaintext
REPOSITORY                  TAG               IMAGE ID       CREATED         SIZE
ubuntu                      20.04             0625b1508e1d   1 minutes ago     14.5GB
```


---

## Review

* `Dockerfile`을 작성할 때는 애플리케이션의 요구사항을 충족하는 환경을 설계하고, 보안과 성능을 균형 있게 고려해야 한다.

    * 특히, 루트 사용자를 대신해 제한된 권한을 가진 사용자 계정을 사용하는 것이 권장된다.

    * 다만, `NOPASSWD` 설정은 개발 환경에서 편리하지만, 프로덕션 환경에서는 보안상 신중히 설정해야 한다.

* 또한, 사용하는 운영체제(OS) 버전에 따라 `Dockerfile`의 명령어와 설정을 조정해야 하며, 애플리케이션의 요구사항에 맞는 적절한 이미지를 선택해야 한다.

* `Dockerfile`은 머릿속으로 알고는 있었지만, 이렇게 글로 정리하니 개념이 더 명확해지고 머릿속이 조금은 편안해진 느낌이다. 이렇게 정리한 글이 다른 사람들에게도 도움이 되었으면 한다.

---

## Reference

* [[Docker 공식문서] dockerfile](https://docs.docker.com/reference/dockerfile/)

* [[Docker 공식문서] docker build](https://docs.docker.com/build/building/best-practices/)