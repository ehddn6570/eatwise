# 🍽️ EatWise - 영양 식단 관리 애플리케이션

## 📋 프로젝트 설명
사용자의 식단을 기록하고, OCR 기술과 AI를 활용하여 영수증에서 음식을 자동으로 인식하고, 사용자의 목표에 맞는 영양 식단을 추천하는 애플리케이션입니다.

## 🚀 시작하기

### 필수 요구사항
- Java 17+
- MySQL 8.0+
- Gradle 7.0+

### 환경 설정

#### 1. Hugging Face API 키 생성
1. https://huggingface.co 접속
2. 회원 가입
3. Settings → Access Tokens → New token
4. 토큰 생성 (hf_로 시작)

#### 2. 환경변수 설정

**Windows PowerShell:**
```powershell
# 임시 설정 (현재 세션에만 적용)
$env:HUGGINGFACE_API_KEY = "hf_your_actual_api_key_here"

# 영구 설정
[Environment]::SetEnvironmentVariable("HUGGINGFACE_API_KEY", "hf_your_actual_api_key_here", "User")
```

**Windows CMD:**
```cmd
set HUGGINGFACE_API_KEY=hf_your_actual_api_key_here
```

#### 3. 데이터베이스 설정

```sql
CREATE DATABASE eatwise CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE eatwise;
```

### 앱 실행

```powershell
cd C:\Users\kim\study\eat\eatwise\eatwise
./gradlew.bat bootRun
```

접속: http://localhost:8080

---

## 🌐 AWS 배포

### 배포 문서
- **[AWS 배포 가이드](./AWS_DEPLOYMENT_GUIDE.md)** - AWS에 프로젝트 배포하기
- **[EC2-RDS 연결 상세 가이드](./EC2_RDS_CONNECTION_GUIDE.md)** - EC2와 RDS 연결 설정 (⭐ 필독)
- **[EC2 설정 자동화 스크립트](./setup-ec2-rds.sh)** - 자동 설정 쉘 스크립트

### 빠른 배포 (자동 설정)

**EC2 인스턴스 접속 후:**

```bash
wget https://raw.githubusercontent.com/[username]/eatwise/main/setup-ec2-rds.sh
chmod +x setup-ec2-rds.sh
./setup-ec2-rds.sh
```

### 수동 배포

더 자세한 수동 배포 과정은 [AWS 배포 가이드](./AWS_DEPLOYMENT_GUIDE.md) 참고

## 🔒 보안 설정

### .gitignore 설정
프로젝트의 `.gitignore` 파일에 다음이 포함되어 있습니다:
```
src/main/resources/application.properties
src/main/resources/application-*.properties
.env
```

### API 키 관리 규칙
- ❌ **절대 하지 말 것**: 실제 API 키를 코드에 하드코딩
- ✅ **해야 할 것**: 환경변수로 관리
- ✅ **참고용**: application.properties.example 사용

## 📚 API 명세

### 식사 기록
- `POST /api/meal-records` - 식사 기록 추가
- `GET /api/meal-records` - 식사 기록 조회

### 음식 정보
- `GET /api/foods/search?keyword=음식명` - 음식 검색
- `POST /api/foods/extract-from-ocr` - OCR에서 음식명 추출

## 🛠️ 기술 스택

- **백엔드**: Spring Boot 3.2+, JPA/Hibernate
- **데이터베이스**: MySQL 8.0
- **프론트엔드**: HTML5, CSS3, JavaScript (Vanilla)
- **OCR**: Tesseract.js
- **AI**: Hugging Face Inference API (Mistral-7B)

## 📖 개발 가이드

### 디렉토리 구조
```
src/main/
├── java/
│   ├── com/project/
│   ├── domain/
│   │   ├── auth/
│   │   ├── food/
│   │   ├── mealrecord/
│   │   └── ...
│   └── global/
│       └── config/
└── resources/
    ├── application.properties
    ├── templates/
    └── static/
```

## ✨ 주요 기능

- ✅ 회원 가입/로그인
- ✅ 식사 기록 (직접 입력/영수증 인식)
- ✅ OCR을 통한 자동 음식 인식
- ✅ AI 기반 영양소 분석
- ✅ 개인화된 식단 추천

## 📝 라이선스

MIT License

## 👥 기여자

- Kim (개발자)

## 📧 문의

문제가 발생하면 GitHub Issues를 통해 보고해주세요.

