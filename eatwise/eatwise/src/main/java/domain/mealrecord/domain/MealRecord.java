package domain.mealrecord.domain;

import domain.food.domain.Food;
import domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_record")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @Column(name = "meal_type")
    private String mealType;

    @Column(name = "intake_time")
    private LocalDateTime intakeTime;

    @Column(name = "intake_date")
    private LocalDate intakeDate;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "total_calories")
    private Double totalCalories;
}
