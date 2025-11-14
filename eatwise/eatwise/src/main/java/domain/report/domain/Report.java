package domain.report.domain;

import domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "report")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column(name = "calories_consumed")
    private Double caloriesConsumed;

    @Column(name = "nutrient_summary", columnDefinition = "TEXT")
    private String nutrientSummary;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;
}
