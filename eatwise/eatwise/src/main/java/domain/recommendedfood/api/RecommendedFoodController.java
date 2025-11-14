package domain.recommendedfood.api;

import domain.recommendedfood.application.RecommendedFoodService;
import domain.recommendedfood.dto.request.RecommendedFoodCreateRequest;
import domain.recommendedfood.dto.request.RecommendedFoodUpdateRequest;
import domain.recommendedfood.dto.response.RecommendedFoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommended-foods")
@RequiredArgsConstructor
public class RecommendedFoodController {

    private final RecommendedFoodService recommendedFoodService;

    @PostMapping
    public ResponseEntity<RecommendedFoodResponse> createRecommendedFood(@RequestBody RecommendedFoodCreateRequest request) {
        RecommendedFoodResponse response = recommendedFoodService.createRecommendedFood(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{recId}")
    public ResponseEntity<RecommendedFoodResponse> getRecommendedFoodById(@PathVariable Long recId) {
        RecommendedFoodResponse response = recommendedFoodService.getRecommendedFoodById(recId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendedFoodResponse>> getRecommendedFoodsByUserId(@PathVariable Long userId) {
        List<RecommendedFoodResponse> responses = recommendedFoodService.getRecommendedFoodsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<RecommendedFoodResponse>> getRecentRecommendedFoodsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") int days) {
        List<RecommendedFoodResponse> responses = recommendedFoodService.getRecentRecommendedFoodsByUserId(userId, days);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{recId}")
    public ResponseEntity<RecommendedFoodResponse> updateRecommendedFood(
            @PathVariable Long recId,
            @RequestBody RecommendedFoodUpdateRequest request) {
        RecommendedFoodResponse response = recommendedFoodService.updateRecommendedFood(recId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{recId}")
    public ResponseEntity<Void> deleteRecommendedFood(@PathVariable Long recId) {
        recommendedFoodService.deleteRecommendedFood(recId);
        return ResponseEntity.noContent().build();
    }
}
