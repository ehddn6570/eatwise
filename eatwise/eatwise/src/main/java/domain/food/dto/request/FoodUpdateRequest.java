package domain.food.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoodUpdateRequest {

    private String foodName;
    private String category;
    private Double calories;
    private Double carbs;
    private Double protein;
    private Double fat;
    private String imageUrl;
}
