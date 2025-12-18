package domain.food.api;

import domain.food.application.FoodService;
import domain.food.dto.request.FoodCreateRequest;
import domain.food.dto.request.FoodUpdateRequest;
import domain.food.dto.response.FoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<FoodResponse> createFood(@RequestBody FoodCreateRequest request) {
        FoodResponse response = foodService.createFood(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{foodId}")
    public ResponseEntity<FoodResponse> getFoodById(@PathVariable Long foodId) {
        FoodResponse response = foodService.getFoodById(foodId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FoodResponse>> getAllFoods() {
        List<FoodResponse> responses = foodService.getAllFoods();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FoodResponse>> getFoodsByCategory(@PathVariable String category) {
        List<FoodResponse> responses = foodService.getFoodsByCategory(category);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodResponse>> searchFoodsByName(@RequestParam String keyword) {
        List<FoodResponse> responses = foodService.searchFoodsByName(keyword);
        return ResponseEntity.ok(responses);
    }

    // OCR 텍스트에서 음식명 추출
    @PostMapping("/extract-from-ocr")
    public ResponseEntity<List<FoodResponse>> extractFoodsFromOCR(@RequestBody String ocrText) {
        List<FoodResponse> results = foodService.extractFoodsFromOCRText(ocrText);
        return ResponseEntity.ok(results);
    }

    @PutMapping("/{foodId}")
    public ResponseEntity<FoodResponse> updateFood(
            @PathVariable Long foodId,
            @RequestBody FoodUpdateRequest request) {
        FoodResponse response = foodService.updateFood(foodId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{foodId}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long foodId) {
        foodService.deleteFood(foodId);
        return ResponseEntity.noContent().build();
    }
}
