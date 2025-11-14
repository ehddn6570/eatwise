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
}
