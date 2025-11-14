# EatWise API 문서

## 기본 정보
- Base URL: `http://localhost:8080`
- Content-Type: `application/json`

---

## 1. User API (사용자)

### 1.1 회원가입
```http
POST /api/users/signup
```

**Request Body:**
```json
{
  "username": "홍길동",
  "password": "password123",
  "email": "hong@example.com",
  "age": 25,
  "gender": "male",
  "height": 175.5,
  "weight": 70.0
}
```

**Response:**
```json
{
  "userId": 1,
  "username": "홍길동",
  "email": "hong@example.com",
  "age": 25,
  "gender": "male",
  "height": 175.5,
  "weight": 70.0
}
```

### 1.2 로그인
```http
POST /api/users/login
```

**Request Body:**
```json
{
  "email": "hong@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "userId": 1,
  "username": "홍길동",
  "email": "hong@example.com",
  "age": 25,
  "gender": "male",
  "height": 175.5,
  "weight": 70.0
}
```

---

## 2. Auth API (인증)

### 2.1 인증 코드 발송
```http
POST /api/auth/send-code?email=hong@example.com
```

**Response:**
```json
{
  "authId": 1,
  "userId": 1,
  "username": "홍길동",
  "email": "hong@example.com",
  "verificationCode": "ABC123",
  "expiresAt": "2024-01-15T10:30:00",
  "verified": false
}
```

### 2.2 인증 코드 검증
```http
POST /api/auth/verify
```

**Request Body:**
```json
{
  "email": "hong@example.com",
  "verificationCode": "ABC123"
}
```

**Response:**
```json
{
  "authId": 1,
  "userId": 1,
  "username": "홍길동",
  "email": "hong@example.com",
  "verificationCode": "ABC123",
  "expiresAt": "2024-01-15T10:30:00",
  "verified": true
}
```

### 2.3 사용자별 인증 정보 조회
```http
GET /api/auth/user/{userId}
```

**Response:**
```json
[
  {
    "authId": 1,
    "userId": 1,
    "username": "홍길동",
    "email": "hong@example.com",
    "verificationCode": "ABC123",
    "expiresAt": "2024-01-15T10:30:00",
    "verified": true
  }
]
```

---

## 3. Food API (음식)

### 3.1 음식 목록 조회
```http
GET /api/foods
```

**Response:**
```json
[
  {
    "foodId": 1,
    "foodName": "닭가슴살",
    "calories": 165.0,
    "protein": 31.0,
    "carbohydrates": 0.0,
    "fat": 3.6,
    "servingSize": 100.0,
    "category": "단백질"
  }
]
```

### 3.2 음식 등록
```http
POST /api/foods
```

**Request Body:**
```json
{
  "foodName": "닭가슴살",
  "calories": 165.0,
  "protein": 31.0,
  "carbohydrates": 0.0,
  "fat": 3.6,
  "servingSize": 100.0,
  "category": "단백질"
}
```

### 3.3 음식 상세 조회
```http
GET /api/foods/{foodId}
```

### 3.4 음식 수정
```http
PUT /api/foods/{foodId}
```

**Request Body:**
```json
{
  "foodName": "닭가슴살",
  "calories": 165.0,
  "protein": 31.0,
  "carbohydrates": 0.0,
  "fat": 3.6,
  "servingSize": 100.0,
  "category": "단백질"
}
```

### 3.5 음식 삭제
```http
DELETE /api/foods/{foodId}
```

### 3.6 카테고리별 음식 조회
```http
GET /api/foods/category/{category}
```

### 3.7 음식 검색
```http
GET /api/foods/search?name=닭
```

---

## 4. MealRecord API (식사 기록)

### 4.1 식사 기록 등록
```http
POST /api/meal-records
```

**Request Body:**
```json
{
  "userId": 1,
  "foodId": 1,
  "mealType": "breakfast",
  "quantity": 150.0,
  "mealDate": "2024-01-15T08:00:00"
}
```

**Response:**
```json
{
  "mealRecordId": 1,
  "userId": 1,
  "username": "홍길동",
  "foodId": 1,
  "foodName": "닭가슴살",
  "mealType": "breakfast",
  "quantity": 150.0,
  "mealDate": "2024-01-15T08:00:00"
}
```

### 4.2 식사 기록 목록 조회
```http
GET /api/meal-records
```

### 4.3 사용자별 식사 기록 조회
```http
GET /api/meal-records/user/{userId}
```

### 4.4 기간별 식사 기록 조회
```http
GET /api/meal-records/user/{userId}/date-range?startDate=2024-01-01&endDate=2024-01-31
```

### 4.5 식사 기록 삭제
```http
DELETE /api/meal-records/{mealRecordId}
```

---

## 5. Goal API (목표)

### 5.1 목표 등록
```http
POST /api/goals
```

**Request Body:**
```json
{
  "userId": 1,
  "goalType": "weight_loss",
  "targetValue": 65.0,
  "currentValue": 70.0,
  "startDate": "2024-01-01",
  "endDate": "2024-03-31",
  "status": "active"
}
```

**Response:**
```json
{
  "goalId": 1,
  "userId": 1,
  "username": "홍길동",
  "goalType": "weight_loss",
  "targetValue": 65.0,
  "currentValue": 70.0,
  "startDate": "2024-01-01",
  "endDate": "2024-03-31",
  "status": "active"
}
```

### 5.2 목표 목록 조회
```http
GET /api/goals
```

### 5.3 사용자별 목표 조회
```http
GET /api/goals/user/{userId}
```

### 5.4 활성 목표 조회
```http
GET /api/goals/user/{userId}/active
```

### 5.5 목표 수정
```http
PUT /api/goals/{goalId}
```

**Request Body:**
```json
{
  "targetValue": 65.0,
  "currentValue": 68.0,
  "status": "active"
}
```

### 5.6 목표 삭제
```http
DELETE /api/goals/{goalId}
```

---

## 6. Notification API (알림)

### 6.1 알림 등록
```http
POST /api/notifications
```

**Request Body:**
```json
{
  "userId": 1,
  "type": "meal_reminder",
  "message": "점심 식사 시간입니다!",
  "createdAt": "2024-01-15T12:00:00",
  "readStatus": false
}
```

**Response:**
```json
{
  "notificationId": 1,
  "userId": 1,
  "username": "홍길동",
  "type": "meal_reminder",
  "message": "점심 식사 시간입니다!",
  "createdAt": "2024-01-15T12:00:00",
  "readStatus": false
}
```

### 6.2 사용자별 알림 조회
```http
GET /api/notifications/user/{userId}
```

### 6.3 읽지 않은 알림 조회
```http
GET /api/notifications/user/{userId}/unread
```

### 6.4 읽지 않은 알림 개수 조회
```http
GET /api/notifications/user/{userId}/unread/count
```

**Response:**
```json
5
```

### 6.5 알림 읽음 처리
```http
PATCH /api/notifications/{notificationId}/read
```

### 6.6 모든 알림 읽음 처리
```http
PATCH /api/notifications/user/{userId}/read-all
```

### 6.7 알림 삭제
```http
DELETE /api/notifications/{notificationId}
```

---

## 7. Restaurant API (식당)

### 7.1 식당 등록
```http
POST /api/restaurants
```

**Request Body:**
```json
{
  "restaurantName": "건강한 식당",
  "address": "서울시 강남구",
  "phoneNumber": "02-1234-5678",
  "category": "한식",
  "rating": 4.5
}
```

**Response:**
```json
{
  "restaurantId": 1,
  "restaurantName": "건강한 식당",
  "address": "서울시 강남구",
  "phoneNumber": "02-1234-5678",
  "category": "한식",
  "rating": 4.5
}
```

### 7.2 식당 목록 조회
```http
GET /api/restaurants
```

### 7.3 카테고리별 식당 조회
```http
GET /api/restaurants/category/{category}
```

### 7.4 식당 검색
```http
GET /api/restaurants/search?name=건강
```

### 7.5 식당 수정
```http
PUT /api/restaurants/{restaurantId}
```

### 7.6 식당 삭제
```http
DELETE /api/restaurants/{restaurantId}
```

---

## 8. RecommendedFood API (추천 음식)

### 8.1 추천 음식 등록
```http
POST /api/recommended-foods
```

**Request Body:**
```json
{
  "userId": 1,
  "foodId": 1,
  "reason": "고단백 저지방 식품으로 다이어트에 적합합니다.",
  "recommendedAt": "2024-01-15T10:00:00"
}
```

**Response:**
```json
{
  "recommendedFoodId": 1,
  "userId": 1,
  "username": "홍길동",
  "foodId": 1,
  "foodName": "닭가슴살",
  "reason": "고단백 저지방 식품으로 다이어트에 적합합니다.",
  "recommendedAt": "2024-01-15T10:00:00"
}
```

### 8.2 사용자별 추천 음식 조회
```http
GET /api/recommended-foods/user/{userId}
```

### 8.3 추천 음식 삭제
```http
DELETE /api/recommended-foods/{recommendedFoodId}
```

---

## 9. Report API (리포트)

### 9.1 리포트 생성
```http
POST /api/reports
```

**Request Body:**
```json
{
  "userId": 1,
  "reportType": "weekly",
  "generatedAt": "2024-01-15T10:00:00",
  "content": "이번 주 총 칼로리 섭취량: 14000kcal"
}
```

**Response:**
```json
{
  "reportId": 1,
  "userId": 1,
  "username": "홍길동",
  "reportType": "weekly",
  "generatedAt": "2024-01-15T10:00:00",
  "content": "이번 주 총 칼로리 섭취량: 14000kcal"
}
```

### 9.2 사용자별 리포트 조회
```http
GET /api/reports/user/{userId}
```

### 9.3 리포트 타입별 조회
```http
GET /api/reports/user/{userId}/type/{reportType}
```

### 9.4 리포트 삭제
```http
DELETE /api/reports/{reportId}
```

---

## 에러 응답 형식

모든 API는 에러 발생 시 다음과 같은 형식으로 응답합니다:

```json
{
  "code": "U001",
  "message": "사용자를 찾을 수 없습니다.",
  "status": 404,
  "timestamp": "2024-01-15T10:00:00",
  "errors": []
}
```

### 주요 에러 코드

| 코드 | 설명 |
|------|------|
| C001 | 내부 서버 오류 |
| C002 | 잘못된 입력값 |
| C004 | 엔티티를 찾을 수 없음 |
| U001 | 사용자를 찾을 수 없음 |
| U002 | 이미 존재하는 이메일 |
| U004 | 비밀번호 불일치 |
| F001 | 음식을 찾을 수 없음 |
| M001 | 식사 기록을 찾을 수 없음 |
| G001 | 목표를 찾을 수 없음 |
| N001 | 알림을 찾을 수 없음 |
| R001 | 식당을 찾을 수 없음 |
| A001 | 인증 정보를 찾을 수 없음 |
| A002 | 인증 코드 만료 |
| A003 | 인증 코드 불일치 |

---

## 참고사항

1. 모든 날짜/시간은 ISO 8601 형식을 사용합니다 (`yyyy-MM-ddTHH:mm:ss`)
2. 모든 요청과 응답은 UTF-8 인코딩을 사용합니다
3. 인증이 필요한 API는 추후 JWT 토큰 기반 인증으로 업그레이드될 예정입니다
4. 페이지네이션은 추후 추가될 예정입니다
