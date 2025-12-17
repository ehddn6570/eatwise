# EatWise AWS 배포 체크리스트

## 📋 사전 준비 사항

### AWS 계정 설정
- [ ] AWS 계정 생성
- [ ] IAM 사용자 생성 (프로그래밍 액세스)
- [ ] AWS 자격증명 설정

### 데이터베이스 준비
- [ ] AWS RDS MySQL 인스턴스 생성
  - 데이터베이스명: eatwise
  - 마스터 사용자: admin
  - 퍼블릭 액세스: YES
  - 보안 그룹 인바운드: MySQL 3306 포트 허용
- [ ] RDS 엔드포인트 복사

### EC2 인스턴스 설정
- [ ] EC2 인스턴스 생성 (Ubuntu 22.04 LTS, t2.micro)
- [ ] 키페어 다운로드
- [ ] 보안 그룹 설정:
  - SSH (22): 내 IP
  - HTTP (80): 0.0.0.0/0
  - HTTPS (443): 0.0.0.0/0
  - Custom (8080): 0.0.0.0/0

---

## 🚀 배포 단계

### 1단계: 로컬에서 빌드
```bash
cd eatwise
./gradlew clean build -x test
```

### 2단계: EC2 인스턴스 접속
```bash
ssh -i your-key.pem ubuntu@your-ec2-public-ip
```

### 3단계: EC2에 필요한 소프트웨어 설치
```bash
# 기본 업데이트
sudo apt update
sudo apt upgrade -y

# Java 17 설치
sudo apt install -y openjdk-17-jdk

# Git 설치
sudo apt install -y git

# MySQL 클라이언트 설치
sudo apt install -y mysql-client
```

### 4단계: 환경변수 설정
```bash
# 환경변수 파일 생성
nano ~/.bashrc
```

파일 끝에 다음 추가:
```bash
export DB_HOST=your-rds-endpoint.region.rds.amazonaws.com
export DB_USERNAME=admin
export DB_PASSWORD=your-rds-password
export AI_PROVIDER=bedrock
export AWS_REGION=ap-northeast-2
```

변경사항 적용:
```bash
source ~/.bashrc
```

### 5단계: 프로젝트 클론 및 배포
```bash
# 프로젝트 클론
git clone https://github.com/your-username/eatwise.git
cd eatwise/eatwise

# 빌드
./gradlew clean build -x test

# 실행
java -jar build/libs/eatwise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 6단계: 백그라운드에서 실행 (nohup 사용)
```bash
# 로그 디렉토리 생성
mkdir -p ~/logs

# 백그라운드에서 실행
nohup java -jar build/libs/eatwise-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > ~/logs/eatwise.log 2>&1 &

# 프로세스 확인
ps aux | grep java
```

### 7단계: 접속 테스트
브라우저에서 접속:
```
http://your-ec2-public-ip:8080
```

---

## 🔧 옵션: Nginx 리버스 프록시 설정 (포트 숨기기)

```bash
# Nginx 설치
sudo apt install -y nginx

# Nginx 설정
sudo nano /etc/nginx/sites-available/default
```

다음 내용으로 수정:
```nginx
server {
    listen 80 default_server;
    listen [::]:80 default_server;

    server_name _;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

설정 적용:
```bash
sudo systemctl restart nginx
sudo systemctl enable nginx
```

그 후 포트 없이 접속 가능:
```
http://your-ec2-public-ip
```

---

## 📊 예상 비용 (월별)

| 서비스 | 사양 | 가격 |
|--------|------|------|
| **EC2** | t2.micro | 무료 (12개월) |
| **RDS** | db.t2.micro | 무료 (12개월) |
| **데이터 전송** | 1GB/월 | 무료 |
| **총계** | - | **무료 (첫 12개월)** |

---

## ⚠️ 보안 주의사항

- [ ] DB 비밀번호를 `환경변수`로 관리 (코드에 절대 하드코딩 금지)
- [ ] EC2 보안 그룹에서 필요한 포트만 열기
- [ ] RDS 퍼블릭 액세스 vs 프라이빗 액세스 선택
- [ ] SSL/TLS 인증서 설정 (AWS Certificate Manager)

---

## 🆘 트러블슈팅

### 데이터베이스 연결 실패
```bash
# RDS 연결 테스트
mysql -h your-rds-endpoint -u admin -p
```

### 애플리케이션이 시작되지 않음
```bash
# 로그 확인
cat ~/logs/eatwise.log
tail -f ~/logs/eatwise.log  # 실시간 보기
```

### 포트 이미 사용 중
```bash
# 포트 확인
sudo lsof -i :8080
# 프로세스 종료
sudo kill -9 <PID>
```

---

## 📚 참고 링크

- [AWS RDS MySQL 생성](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_GettingStarted.CreatingConnecting.MySQL.html)
- [AWS EC2 시작하기](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/EC2_GetStarted.html)
- [Spring Boot 배포 가이드](https://spring.io/guides/gs/spring-boot/)

