package domain.recommendedfood.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedFoodCreateRequest {

    private Long userId;
    private Long foodId;
    private String reason;
    private LocalDateTime createdAt;
}
