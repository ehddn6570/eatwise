package domain.mealrecord.dto.response;

import domain.mealrecord.domain.MealRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private String mealType;

    private String intakeTime;      // LocalDateTime을 String으로 변환
    private String intakeDate;      // LocalDate를 String으로 변환

    private Double quantity;
    private Double totalCalories;

    private FoodInfo food;          // 음식 정보
    private List<FoodInfo> foods;   // 식사에 포함된 음식 목록

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FoodInfo {
        private Long foodId;
        private String foodName;
        private String category;
        private Double calories;
        private Double carbs;
        private Double protein;
        private Double fat;
        private Double quantity;
    }

    public static MealRecordResponse from(MealRecord mealRecord) {
        String intakeDateStr = mealRecord.getIntakeDate() != null
                ? mealRecord.getIntakeDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : null;

        String intakeTimeStr = mealRecord.getIntakeTime() != null
                ? mealRecord.getIntakeTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                : null;


        return MealRecordResponse.builder()
                .recordId(mealRecord.getRecordId())
                .userId(mealRecord.getUser().getUserId())
                .username(mealRecord.getUser().getUsername())
                .foodId(mealRecord.getFood().getFoodId())
                .foodName(mealRecord.getFood().getFoodName())
                .mealType(mealRecord.getMealType())
                .intakeTime(intakeTimeStr)
                .intakeDate(intakeDateStr)
                .quantity(mealRecord.getQuantity())
                .totalCalories(mealRecord.getTotalCalories())
                .food(FoodInfo.builder()
                    .foodId(mealRecord.getFood().getFoodId())
                    .foodName(mealRecord.getFood().getFoodName())
                    .category(mealRecord.getFood().getCategory())
                    .calories(mealRecord.getFood().getCalories())
                    .carbs(mealRecord.getFood().getCarbs())
                    .protein(mealRecord.getFood().getProtein())
                    .fat(mealRecord.getFood().getFat())
                    .quantity(mealRecord.getQuantity())
                    .build())
                .foods(List.of(
                    FoodInfo.builder()
                        .foodId(mealRecord.getFood().getFoodId())
                        .foodName(mealRecord.getFood().getFoodName())
                        .category(mealRecord.getFood().getCategory())
                        .calories(mealRecord.getFood().getCalories() * mealRecord.getQuantity() / 100)
                        .carbs(mealRecord.getFood().getCarbs())
                        .protein(mealRecord.getFood().getProtein())
                        .fat(mealRecord.getFood().getFat())
                        .quantity(mealRecord.getQuantity())
                        .build()
                ))
                .build();
    }
}
