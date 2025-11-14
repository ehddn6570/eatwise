package domain.recommendedfood.application;

import domain.food.dao.FoodRepository;
import domain.food.domain.Food;
import domain.recommendedfood.dao.RecommendedFoodRepository;
import domain.recommendedfood.domain.RecommendedFood;
import domain.recommendedfood.dto.request.RecommendedFoodCreateRequest;
import domain.recommendedfood.dto.request.RecommendedFoodUpdateRequest;
import domain.recommendedfood.dto.response.RecommendedFoodResponse;
import domain.user.dao.UserRepository;
import domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendedFoodService {

    private final RecommendedFoodRepository recommendedFoodRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    @Transactional
    public RecommendedFoodResponse createRecommendedFood(RecommendedFoodCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + request.getFoodId()));

        RecommendedFood recommendedFood = RecommendedFood.builder()
                .user(user)
                .food(food)
                .reason(request.getReason())
                .createdAt(request.getCreatedAt() != null ? request.getCreatedAt() : LocalDateTime.now())
                .build();

        RecommendedFood savedRecommendedFood = recommendedFoodRepository.save(recommendedFood);
        return RecommendedFoodResponse.from(savedRecommendedFood);
    }

    public RecommendedFoodResponse getRecommendedFoodById(Long recId) {
        RecommendedFood recommendedFood = recommendedFoodRepository.findById(recId)
                .orElseThrow(() -> new IllegalArgumentException("RecommendedFood not found with id: " + recId));
        return RecommendedFoodResponse.from(recommendedFood);
    }

    public List<RecommendedFoodResponse> getRecommendedFoodsByUserId(Long userId) {
        return recommendedFoodRepository.findByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(RecommendedFoodResponse::from)
                .collect(Collectors.toList());
    }

    public List<RecommendedFoodResponse> getRecentRecommendedFoodsByUserId(Long userId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return recommendedFoodRepository.findByUser_UserIdAndCreatedAtAfter(userId, since).stream()
                .map(RecommendedFoodResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecommendedFoodResponse updateRecommendedFood(Long recId, RecommendedFoodUpdateRequest request) {
        RecommendedFood recommendedFood = recommendedFoodRepository.findById(recId)
                .orElseThrow(() -> new IllegalArgumentException("RecommendedFood not found with id: " + recId));

        RecommendedFood updatedRecommendedFood = RecommendedFood.builder()
                .recId(recommendedFood.getRecId())
                .user(recommendedFood.getUser())
                .food(recommendedFood.getFood())
                .reason(request.getReason())
                .createdAt(recommendedFood.getCreatedAt())
                .build();

        RecommendedFood savedRecommendedFood = recommendedFoodRepository.save(updatedRecommendedFood);
        return RecommendedFoodResponse.from(savedRecommendedFood);
    }

    @Transactional
    public void deleteRecommendedFood(Long recId) {
        if (!recommendedFoodRepository.existsById(recId)) {
            throw new IllegalArgumentException("RecommendedFood not found with id: " + recId);
        }
        recommendedFoodRepository.deleteById(recId);
    }
}
