package domain.mealrecord.api;

import domain.mealrecord.application.MealRecordService;
import domain.mealrecord.dto.request.MealRecordCreateRequest;
import domain.mealrecord.dto.request.MealRecordUpdateRequest;
import domain.mealrecord.dto.response.MealRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meal-records")
@RequiredArgsConstructor
public class MealRecordController {

    private final MealRecordService mealRecordService;

    @PostMapping
    public ResponseEntity<MealRecordResponse> createMealRecord(@RequestBody MealRecordCreateRequest request) {
        MealRecordResponse response = mealRecordService.createMealRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<MealRecordResponse> getMealRecordById(@PathVariable Long recordId) {
        MealRecordResponse response = mealRecordService.getMealRecordById(recordId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MealRecordResponse>> getMealRecordsByUserId(@PathVariable Long userId) {
        List<MealRecordResponse> responses = mealRecordService.getMealRecordsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<List<MealRecordResponse>> getMealRecordsByUserIdAndDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<MealRecordResponse> responses = mealRecordService.getMealRecordsByUserIdAndDate(userId, date);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<MealRecordResponse>> getMealRecordsByUserIdAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<MealRecordResponse> responses = mealRecordService.getMealRecordsByUserIdAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<MealRecordResponse> updateMealRecord(
            @PathVariable Long recordId,
            @RequestBody MealRecordUpdateRequest request) {
        MealRecordResponse response = mealRecordService.updateMealRecord(recordId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteMealRecord(@PathVariable Long recordId) {
        mealRecordService.deleteMealRecord(recordId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/period")
    public ResponseEntity<List<MealRecordResponse>> getMealRecordsByPeriod(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<MealRecordResponse> responses = mealRecordService.getMealRecordsByUserIdAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    // OCR 영수증 인식
    @PostMapping("/ocr")
    public ResponseEntity<?> processReceiptOcr(@RequestBody OcrRequest request) {
        OcrResponse response = new OcrResponse();
        response.setMessage("영수증이 인식되었습니다.");

        // 프론트엔드에서 전달받은 음식 데이터 사용
        java.util.List<OcrResponse.FoodItem> foods = new java.util.ArrayList<>();
        if (request.getFoods() != null && !request.getFoods().isEmpty()) {
            for (OcrRequest.FoodItem food : request.getFoods()) {
                foods.add(new OcrResponse.FoodItem(food.getName(), food.getQuantity()));
            }
        }

        // 만약 음식이 없으면 빈 목록 반환
        response.setFoods(foods);

        return ResponseEntity.ok(response);
    }

    // OCR 요청 DTO
    public static class OcrRequest {
        private Long userId;
        private String imageBase64;
        private String intakeDate;
        private String mealType;
        private String recognizedText;
        private java.util.List<FoodItem> foods;

        // Getter, Setter
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getImageBase64() { return imageBase64; }
        public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
        public String getIntakeDate() { return intakeDate; }
        public void setIntakeDate(String intakeDate) { this.intakeDate = intakeDate; }
        public String getMealType() { return mealType; }
        public void setMealType(String mealType) { this.mealType = mealType; }
        public String getRecognizedText() { return recognizedText; }
        public void setRecognizedText(String recognizedText) { this.recognizedText = recognizedText; }
        public java.util.List<FoodItem> getFoods() { return foods; }
        public void setFoods(java.util.List<FoodItem> foods) { this.foods = foods; }

        public static class FoodItem {
            private String name;
            private String quantity;

            public FoodItem() {}
            public FoodItem(String name, String quantity) {
                this.name = name;
                this.quantity = quantity;
            }

            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
            public String getQuantity() { return quantity; }
            public void setQuantity(String quantity) { this.quantity = quantity; }
        }
    }

    // OCR 응답 DTO
    public static class OcrResponse {
        private String message;
        private java.util.List<FoodItem> foods;

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public java.util.List<FoodItem> getFoods() { return foods; }
        public void setFoods(java.util.List<FoodItem> foods) { this.foods = foods; }

        public static class FoodItem {
            private String name;
            private String quantity;

            public FoodItem(String name, String quantity) {
                this.name = name;
                this.quantity = quantity;
            }

            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
            public String getQuantity() { return quantity; }
            public void setQuantity(String quantity) { this.quantity = quantity; }
        }
    }
}
