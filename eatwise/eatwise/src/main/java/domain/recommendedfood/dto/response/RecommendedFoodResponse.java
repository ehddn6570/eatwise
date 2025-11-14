package domain.recommendedfood.dto.response;

import domain.recommendedfood.domain.RecommendedFood;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendedFoodResponse {

    private Long recId;
    private Long userId;
    private String username;
    private Long foodId;
    private String foodName;
    private String reason;
    private LocalDateTime createdAt;

    public static RecommendedFoodResponse from(RecommendedFood recommendedFood) {
        return RecommendedFoodResponse.builder()
                .recId(recommendedFood.getRecId())
                .userId(recommendedFood.getUser().getUserId())
                .username(recommendedFood.getUser().getUsername())
                .foodId(recommendedFood.getFood().getFoodId())
                .foodName(recommendedFood.getFood().getFoodName())
                .reason(recommendedFood.getReason())
                .createdAt(recommendedFood.getCreatedAt())
                .build();
    }
}
