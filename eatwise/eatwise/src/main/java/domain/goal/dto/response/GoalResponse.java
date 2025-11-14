package domain.goal.dto.response;

import domain.goal.domain.Goal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalResponse {

    private Long goalId;
    private Long userId;
    private String username;
    private Double dailyCalorieTarget;
    private Double carbRatio;
    private Double proteinRatio;
    private Double fatRatio;
    private LocalDate startDate;
    private LocalDate endDate;

    public static GoalResponse from(Goal goal) {
        return GoalResponse.builder()
                .goalId(goal.getGoalId())
                .userId(goal.getUser().getUserId())
                .username(goal.getUser().getUsername())
                .dailyCalorieTarget(goal.getDailyCalorieTarget())
                .carbRatio(goal.getCarbRatio())
                .proteinRatio(goal.getProteinRatio())
                .fatRatio(goal.getFatRatio())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .build();
    }
}
