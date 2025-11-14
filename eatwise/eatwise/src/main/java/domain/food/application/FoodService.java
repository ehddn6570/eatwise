package domain.food.application;

import domain.food.dao.FoodRepository;
import domain.food.domain.Food;
import domain.food.dto.request.FoodCreateRequest;
import domain.food.dto.request.FoodUpdateRequest;
import domain.food.dto.response.FoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodService {

    private final FoodRepository foodRepository;

    @Transactional
    public FoodResponse createFood(FoodCreateRequest request) {
        Food food = Food.builder()
                .foodName(request.getFoodName())
                .category(request.getCategory())
                .calories(request.getCalories())
                .carbs(request.getCarbs())
                .protein(request.getProtein())
                .fat(request.getFat())
                .imageUrl(request.getImageUrl())
                .build();

        Food savedFood = foodRepository.save(food);
        return FoodResponse.from(savedFood);
    }

    public FoodResponse getFoodById(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + foodId));
        return FoodResponse.from(food);
    }

    public List<FoodResponse> getAllFoods() {
        return foodRepository.findAll().stream()
                .map(FoodResponse::from)
                .collect(Collectors.toList());
    }

    public List<FoodResponse> getFoodsByCategory(String category) {
        return foodRepository.findByCategory(category).stream()
                .map(FoodResponse::from)
                .collect(Collectors.toList());
    }

    public List<FoodResponse> searchFoodsByName(String keyword) {
        return foodRepository.findByFoodNameContaining(keyword).stream()
                .map(FoodResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public FoodResponse updateFood(Long foodId, FoodUpdateRequest request) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + foodId));

        Food updatedFood = Food.builder()
                .foodId(food.getFoodId())
                .foodName(request.getFoodName())
                .category(request.getCategory())
                .calories(request.getCalories())
                .carbs(request.getCarbs())
                .protein(request.getProtein())
                .fat(request.getFat())
                .imageUrl(request.getImageUrl())
                .build();

        Food savedFood = foodRepository.save(updatedFood);
        return FoodResponse.from(savedFood);
    }

    @Transactional
    public void deleteFood(Long foodId) {
        if (!foodRepository.existsById(foodId)) {
            throw new IllegalArgumentException("Food not found with id: " + foodId);
        }
        foodRepository.deleteById(foodId);
    }
}
