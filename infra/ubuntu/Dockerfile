# Ubuntu 20.04
FROM ubuntu:20.04

# 메타데이터 추가
LABEL service="GoldPay"
LABEL authors="devfancy"
LABEL email="hello.devfancy@gmail.com"

# 빌드 시점 변수 설정
ARG WORKDIR=/app/GoldPay
ARG username=user
ARG userid=1000
ARG groupname=user
ARG groupid=1000

# 환경 변수 설정
ENV WORKDIR=${WORKDIR}

# 필수 패키지 업데이트 및 설치
RUN apt-get update && apt-get -y upgrade && \
    apt-get -y install sudo which wget gzip openjdk-11-jdk && \
    apt-get clean

# 사용자 및 그룹 생성
RUN groupadd -g $groupid $groupname && \
    groupadd wheel && \
    useradd -u $userid -g $groupname -m $username -s /bin/bash && \
    usermod -aG wheel $username && \
    echo "$username ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers.d/$username

# 작업 디렉토리 생성 및 소유권 설정
RUN mkdir -p ${WORKDIR} && \
    chown $username:$groupname -R ${WORKDIR}

# 애플리케이션 파일 복사
COPY ./app ${WORKDIR}/

# 사용자 변경 (보안을 위해 루트 대신 사용자 계정으로 실행)
USER $username:$groupname

# 작업 디렉토리 설정
WORKDIR ${WORKDIR}