package domain.mealrecord.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MealRecordUpdateRequest {

    private LocalDateTime intakeTime;
    private LocalDate intakeDate;
    private Double quantity;
    private Double totalCalories;
}
