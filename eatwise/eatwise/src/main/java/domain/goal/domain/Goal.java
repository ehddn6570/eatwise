package domain.goal.domain;

import domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "goal")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "goal_type")
    @Enumerated(EnumType.STRING)
    private GoalType goalType;  // GAIN(증량), MAINTAIN(유지), LOSE(감량)

    @Column(name = "daily_calorie_target")
    private Double dailyCalorieTarget;

    @Column(name = "carb_ratio")
    private Double carbRatio;

    @Column(name = "protein_ratio")
    private Double proteinRatio;

    @Column(name = "fat_ratio")
    private Double fatRatio;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public enum GoalType {
        GAIN("체중 증량"),
        MAINTAIN("체중 유지"),
        LOSE("체중 감량");

        private final String label;

        GoalType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
