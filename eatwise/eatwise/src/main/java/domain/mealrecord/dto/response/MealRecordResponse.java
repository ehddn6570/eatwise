package domain.mealrecord.dto.response;

import domain.mealrecord.domain.MealRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealRecordResponse {

    private Long recordId;
    private Long userId;
    private String username;
    private Long foodId;
    private String foodName;
    private LocalDateTime intakeTime;
    private LocalDate intakeDate;
    private Double quantity;
    private Double totalCalories;

    public static MealRecordResponse from(MealRecord mealRecord) {
        return MealRecordResponse.builder()
                .recordId(mealRecord.getRecordId())
                .userId(mealRecord.getUser().getUserId())
                .username(mealRecord.getUser().getUsername())
                .foodId(mealRecord.getFood().getFoodId())
                .foodName(mealRecord.getFood().getFoodName())
                .intakeTime(mealRecord.getIntakeTime())
                .intakeDate(mealRecord.getIntakeDate())
                .quantity(mealRecord.getQuantity())
                .totalCalories(mealRecord.getTotalCalories())
                .build();
    }
}
