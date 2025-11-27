# CentOS 8.4.2105
FROM centos:8.4.2105

# 메타데이터 추가
LABEL authors="devfancy"
LABEL email="fancy.juyongmoon@gmail.com"

# 빌드 시점 변수 설정
ARG username=user
ARG userid=1000
ARG groupname=user
ARG groupid=1000

RUN if [ -d /etc/yum.repos.d ]; then \
        /usr/bin/sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-Linux-* && \
        /usr/bin/sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-Linux-*; \
    fi

# 필수 패키지 업데이트 및 설치
RUN yum update -y
RUN yum install -y \
    sudo \
    which \
    wget \
    gzip \
    java-11-openjdk
RUN yum clean all

# Java 환경 변수 설정
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk
ENV PATH $PATH:${JAVA_HOME}/bin

# 사용자 및 그룹 생성
RUN getent group $groupname || groupadd -g $groupid $groupname
RUN getent group wheel || groupadd wheel
RUN useradd -u $userid -g $groupname -m $username -s /bin/bash
RUN usermod -aG wheel $username
RUN echo "$username ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers.d/$username

# 작업 디렉토리 생성 및 소유권 설정
RUN if [ ! -d "/app" ]; then mkdir /app; else echo "/app already exists"; fi && \
    chown $username:$groupname -R /app

# 사용자 변경 (보안을 위해 루트 대신 사용자 계정으로 실행)
USER $username:$groupname

# 작업 디렉토리 설정
WORKDIR /app
