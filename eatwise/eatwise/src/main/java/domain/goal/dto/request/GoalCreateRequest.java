package domain.goal.dto.request;

import domain.goal.domain.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoalCreateRequest {

    private Long userId;
    private Goal.GoalType goalType;  // GAIN, MAINTAIN, LOSE
    private Double dailyCalorieTarget;
    private Double carbRatio;
    private Double proteinRatio;
    private Double fatRatio;
    private LocalDate startDate;
    private LocalDate endDate;
}
