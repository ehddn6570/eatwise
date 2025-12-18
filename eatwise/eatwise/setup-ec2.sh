#!/bin/bash

# Java 설치
sudo apt update
sudo apt install -y openjdk-17-jdk

# MySQL 클라이언트 설치
sudo apt install -y mysql-client

# Git 설치
sudo apt install -y git

# Nginx 설치 (리버스 프록시용, 선택사항)
sudo apt install -y nginx

echo "설치 완료!"

