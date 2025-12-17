# EC2 + RDS 연결 - 빠른 참고서

## 📌 5분 요약

### 1️⃣ 보안 그룹 설정
```
EC2 보안 그룹 (인바운드):
  - SSH: 22 (내 IP)
  - HTTP: 80 (0.0.0.0/0)
  - Custom TCP: 8080 (0.0.0.0/0)

RDS 보안 그룹 (인바운드):
  - MySQL: 3306 (소스: EC2 보안 그룹 ID)
```

### 2️⃣ RDS 정보 확인
```
✅ RDS Endpoint: eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com
Port: 3306
Database: eatwise
```

### 3️⃣ EC2 환경변수 설정
```bash
# SSH 접속 후
nano ~/.bashrc

# 맨 끝에 추가:
export DB_HOST=eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com
export DB_USERNAME=admin
export DB_PASSWORD=12345678
export DB_NAME=eatwise
export SPRING_PROFILES_ACTIVE=prod

# 저장 후
source ~/.bashrc
```

### 4️⃣ RDS 연결 테스트
```bash
mysql -h eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com -u admin -p12345678
SHOW DATABASES;
EXIT;
```

### 5️⃣ 애플리케이션 실행
```bash
# 빌드
./gradlew clean build -x test

# 실행
java -jar build/libs/eatwise-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod
```

---

## 🚀 자동 설정 스크립트 사용

**EC2 인스턴스에서:**

```bash
wget https://raw.githubusercontent.com/[username]/eatwise/main/setup-ec2-rds.sh
chmod +x setup-ec2-rds.sh
./setup-ec2-rds.sh
```

스크립트 실행 시 다음을 입력:
- RDS 엔드포인트: `eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com`
- RDS 사용자명: `admin`
- RDS 비밀번호: `12345678` (Enter 누르면 기본값 사용)
- GitHub URL: `[저장소 URL]`

---

## ❌ 연결 실패 시 체크리스트

- [ ] RDS 상태: AWS Console에서 "Available" 확인
- [ ] EC2 상태: "Running" 확인
- [ ] 보안 그룹 인바운드 규칙 확인
  - EC2 SG: SSH(22), HTTP(80), Custom TCP(8080)
  - RDS SG: MySQL(3306) from EC2 SG ID
- [ ] RDS 엔드포인트: `eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com` 확인
- [ ] DB_HOST, DB_USERNAME, DB_PASSWORD 환경변수 확인
- [ ] 테스트: `mysql -h eatwise.cluae06eww4b.ap-northeast-2.rds.amazonaws.com -u admin -p`

---

## 📚 더 자세한 정보

⭐ **[EC2_RDS_CONNECTION_GUIDE.md](./EC2_RDS_CONNECTION_GUIDE.md)** 전체 가이드 참고
📖 **[AWS_DEPLOYMENT_GUIDE.md](./AWS_DEPLOYMENT_GUIDE.md)** AWS 배포 체크리스트 참고

