# EatWise - 영양 관리 애플리케이션

## 📋 프로젝트 개요
EatWise는 사용자의 식단 관리와 영양 목표 달성을 돕는 종합 영양 관리 애플리케이션입니다.

## 🛠 기술 스택

### Backend
- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Database**: MySQL
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security
- **Build Tool**: Gradle

### 주요 라이브러리
- Lombok
- Spring Boot DevTools
- MySQL Connector

## 📁 프로젝트 구조

```
eatwise/
├── src/main/java/
│   ├── com.project.eatwise/
│   │   └── EatwiseApplication.java
│   ├── domain/
│   │   ├── user/              # 사용자 도메인
│   │   ├── auth/              # 인증 도메인
│   │   ├── food/              # 음식 도메인
│   │   ├── mealrecord/        # 식사 기록 도메인
│   │   ├── goal/              # 목표 도메인
│   │   ├── notification/      # 알림 도메인
│   │   ├── restaurant/        # 식당 도메인
│   │   ├── recommendedfood/   # 추천 음식 도메인
│   │   └── report/            # 리포트 도메인
│   └── global/
│       ├── config/            # 설정 파일
│       ├── exception/         # 예외 처리
│       ├── common/            # 공통 클래스
│       └── util/              # 유틸리티
└── src/main/resources/
    └── application.properties
```

## 🎯 주요 기능

### 1. 사용자 관리 (User)
- 회원가입 / 로그인
- 사용자 프로필 관리 (나이, 성별, 키, 몸무게)
- BCrypt 비밀번호 암호화

### 2. 인증 (Auth)
- 이메일 인증 코드 발송
- 인증 코드 검증
- 인증 만료 시간 관리

### 3. 음식 관리 (Food)
- 음식 정보 등록 (칼로리, 단백질, 탄수화물, 지방)
- 카테고리별 음식 조회
- 음식 검색

### 4. 식사 기록 (MealRecord)
- 식사 기록 등록 (아침, 점심, 저녁, 간식)
- 날짜별 식사 기록 조회
- 기간별 식사 기록 통계

### 5. 목표 관리 (Goal)
- 영양 목표 설정 (체중 감량, 근육 증가 등)
- 목표 진행 상황 추적
- 목표 달성률 계산

### 6. 알림 (Notification)
- 식사 시간 알림
- 목표 달성 알림
- 읽음/안읽음 상태 관리

### 7. 식당 정보 (Restaurant)
- 식당 정보 등록
- 카테고리별 식당 조회
- 평점 관리

### 8. 음식 추천 (RecommendedFood)
- 사용자 맞춤 음식 추천
- 추천 이유 제공

### 9. 리포트 (Report)
- 영양 섭취 리포트 생성
- 기간별 통계 리포트
- 리포트 타입별 조회

## 🚀 시작하기

### 사전 요구사항
- JDK 17 이상
- MySQL 8.0 이상
- Gradle 7.0 이상

### 데이터베이스 설정
```sql
CREATE DATABASE eatwise CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### application.properties 설정
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/eatwise?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 애플리케이션 실행
```bash
# Gradle을 사용한 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

서버는 기본적으로 `http://localhost:8080`에서 실행됩니다.

## 📡 API 엔드포인트

### User API
- `POST /api/users/signup` - 회원가입
- `POST /api/users/login` - 로그인

### Auth API
- `POST /api/auth/send-code` - 인증 코드 발송
- `POST /api/auth/verify` - 인증 코드 검증

### Food API
- `GET /api/foods` - 음식 목록 조회
- `POST /api/foods` - 음식 등록
- `GET /api/foods/{id}` - 음식 상세 조회
- `PUT /api/foods/{id}` - 음식 수정
- `DELETE /api/foods/{id}` - 음식 삭제

### MealRecord API
- `GET /api/meal-records` - 식사 기록 목록 조회
- `POST /api/meal-records` - 식사 기록 등록
- `GET /api/meal-records/user/{userId}` - 사용자별 식사 기록 조회

### Goal API
- `GET /api/goals` - 목표 목록 조회
- `POST /api/goals` - 목표 등록
- `GET /api/goals/user/{userId}` - 사용자별 목표 조회

### Notification API
- `GET /api/notifications/user/{userId}` - 사용자별 알림 조회
- `GET /api/notifications/user/{userId}/unread` - 읽지 않은 알림 조회
- `PATCH /api/notifications/{id}/read` - 알림 읽음 처리

### Restaurant API
- `GET /api/restaurants` - 식당 목록 조회
- `POST /api/restaurants` - 식당 등록
- `GET /api/restaurants/category/{category}` - 카테고리별 식당 조회

### RecommendedFood API
- `GET /api/recommended-foods/user/{userId}` - 사용자별 추천 음식 조회
- `POST /api/recommended-foods` - 추천 음식 등록

### Report API
- `GET /api/reports/user/{userId}` - 사용자별 리포트 조회
- `POST /api/reports` - 리포트 생성

## 🔒 보안 설정

### Spring Security
- CSRF 비활성화 (REST API)
- Stateless 세션 관리
- BCrypt 비밀번호 암호화
- CORS 설정 (React 개발 서버 허용)

### 허용된 엔드포인트
- `/api/users/signup`
- `/api/users/login`
- `/api/auth/**`

## 🗄 데이터베이스 스키마

### 주요 테이블
- `users` - 사용자 정보
- `auth` - 인증 정보
- `food` - 음식 정보
- `meal_record` - 식사 기록
- `goal` - 목표 정보
- `notification` - 알림 정보
- `restaurant` - 식당 정보
- `recommended_food` - 추천 음식
- `report` - 리포트 정보

## 🎨 코드 컨벤션

### 패키지 구조
각 도메인은 다음과 같은 구조를 따릅니다:
```
domain/{domain_name}/
├── api/           # Controller
├── application/   # Service
├── dao/           # Repository
├── domain/        # Entity
└── dto/
    ├── request/   # Request DTO
    └── response/  # Response DTO
```

### 네이밍 규칙
- Entity: 단수형 (User, Food)
- Table: 소문자 (users, food)
- ID 필드: {entityName}Id (userId, foodId)
- Repository: {Entity}Repository
- Service: {Entity}Service
- Controller: {Entity}Controller

## 🧪 테스트
```bash
./gradlew test
```

## 📝 라이선스
이 프로젝트는 학습 목적으로 제작되었습니다.

## 👥 개발자
- Backend Developer: [Your Name]

## 📞 문의
프로젝트 관련 문의사항이 있으시면 이슈를 등록해주세요.
