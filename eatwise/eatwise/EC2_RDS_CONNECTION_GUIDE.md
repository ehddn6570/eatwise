# EC2와 RDS 연결 상세 가이드

이 문서는 AWS EC2 인스턴스와 RDS 데이터베이스를 안전하고 정상적으로 연결하는 방법을 단계별로 설명합니다.

## 실제 RDS 정보
```
RDS Endpoint: eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com
Port: 3306
Database: eatwise
Master Username: admin
```

---

## 🔐 보안 그룹 설정

### RDS 보안 그룹 인바운드 규칙

| 유형 | 프로토콜 | 포트 범위 | 소스 | 설명 |
|------|---------|---------|------|------|
| MySQL/Aurora | TCP | 3306 | EC2 보안 그룹 ID | EC2에서의 접속 허용 |

### EC2 보안 그룹 인바운드 규칙

| 유형 | 프로토콜 | 포트 | 소스 | 설명 |
|------|---------|------|------|------|
| SSH | TCP | 22 | 내 IP | EC2 접속 |
| HTTP | TCP | 80 | 0.0.0.0/0 | 웹 브라우저 접속 |
| HTTPS | TCP | 443 | 0.0.0.0/0 | SSL 연결 |
| Custom TCP | TCP | 8080 | 0.0.0.0/0 | 애플리케이션 포트 |

---

## 💻 EC2에서 환경변수 설정

### 1단계: EC2 인스턴스 SSH 접속

```bash
ssh -i "your-key.pem" ubuntu@[EC2 퍼블릭 IP]
```

### 2단계: 필수 소프트웨어 설치

```bash
sudo apt update && sudo apt upgrade -y
sudo apt install -y openjdk-17-jdk git mysql-client nano
```

### 3단계: 환경변수 설정

```bash
nano ~/.bashrc
```

파일 맨 끝에 추가:

```bash
# ==================== EatWise RDS 연결 설정 ====================
export DB_HOST=eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com
export DB_USERNAME=admin
export DB_PASSWORD=12345678
export DB_PORT=3306
export DB_NAME=eatwise

# AI 설정
export AI_PROVIDER=bedrock
export AWS_REGION=ap-northeast-2

# Spring Boot 설정
export SPRING_PROFILES_ACTIVE=prod

# 로그 디렉토리
export LOG_HOME=$HOME/logs
# =================================================================
```

### 4단계: 환경변수 적용 및 확인

```bash
source ~/.bashrc
echo $DB_HOST
mkdir -p ~/logs
```

---

## 🔗 RDS 연결 테스트

### MySQL 클라이언트로 연결 테스트

```bash
mysql -h eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com -u admin -p12345678
```

비밀번호 입력 후:

```sql
SHOW DATABASES;
USE eatwise;
SHOW TABLES;
EXIT;
```

### 포트 통신 확인

```bash
nc -zv eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com 3306
```

---

## 🚀 애플리케이션 배포

### 1단계: 프로젝트 클론

```bash
git clone https://github.com/[username]/eatwise.git
cd eatwise/eatwise
```

### 2단계: 빌드

```bash
./gradlew clean build -x test
```

### 3단계: 실행

#### 포그라운드 (테스트)

```bash
java -jar build/libs/eatwise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

#### 백그라운드 (프로덕션)

```bash
nohup java -jar build/libs/eatwise-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  > ~/logs/eatwise.log 2>&1 &

# 로그 확인
tail -f ~/logs/eatwise.log
```

#### Systemd 서비스 (권장)

```bash
sudo tee /etc/systemd/system/eatwise.service > /dev/null << EOF
[Unit]
Description=EatWise Application
After=network.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=$(pwd)
ExecStart=/usr/bin/java -jar build/libs/eatwise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
Restart=on-failure
RestartSec=10
StandardOutput=append:$HOME/logs/eatwise.log
StandardError=append:$HOME/logs/eatwise.log

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable eatwise.service
sudo systemctl start eatwise.service
sudo systemctl status eatwise.service
```

---

## 🌐 웹 브라우저 접속

```
http://[EC2 퍼블릭 IP]:8080
```

---

## 🆘 트러블슈팅

### 1. MySQL 연결 실패

```bash
# 환경변수 확인
echo "DB_HOST: $DB_HOST"
echo "DB_USERNAME: $DB_USERNAME"

# DNS 해석 확인
nslookup eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com

# 포트 통신 확인
nc -zv eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com 3306

# MySQL 직접 연결 테스트
mysql -h eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com -u admin -p
```

**체크리스트:**
- [ ] RDS 상태: "Available" (AWS Console)
- [ ] RDS 보안 그룹: MySQL 3306 (EC2 SG 출처) 허용
- [ ] EC2 보안 그룹: Custom TCP 8080 허용
- [ ] 엔드포인트 공백/오타 확인

### 2. 애플리케이션 시작 실패

```bash
# 실시간 로그 확인
tail -f ~/logs/eatwise.log

# 프로세스 확인
ps aux | grep java

# 포트 사용 확인
sudo lsof -i :8080
```

### 3. 데이터베이스 미존재

```bash
mysql -h eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com -u admin -p
CREATE DATABASE eatwise CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

---

## ✅ 체크리스트

- [ ] RDS 인스턴스 생성 및 "Available" 확인
- [ ] EC2 인스턴스 생성 및 "Running" 확인
- [ ] 보안 그룹 인바운드 규칙 설정 완료
- [ ] Java 17 설치 완료
- [ ] 환경변수 설정 완료
- [ ] MySQL 연결 테스트 성공
- [ ] 프로젝트 빌드 성공
- [ ] 애플리케이션 시작 성공
- [ ] 브라우저 접속 성공

---

## 📞 자동 설정

자동 설정 스크립트 사용:

```bash
wget https://raw.githubusercontent.com/[username]/eatwise/main/setup-ec2-rds.sh
chmod +x setup-ec2-rds.sh
./setup-ec2-rds.sh
```

