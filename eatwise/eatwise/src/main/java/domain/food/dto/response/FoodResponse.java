package domain.food.dto.response;

import domain.food.domain.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodResponse {

    private Long foodId;
    private String foodName;
    private String category;
    private Double calories;
    private Double carbs;
    private Double protein;
    private Double fat;
    private String imageUrl;

    public static FoodResponse from(Food food) {
        return FoodResponse.builder()
                .foodId(food.getFoodId())
                .foodName(food.getFoodName())
                .category(food.getCategory())
                .calories(food.getCalories())
                .carbs(food.getCarbs())
                .protein(food.getProtein())
                .fat(food.getFat())
                .imageUrl(food.getImageUrl())
                .build();
    }
}
