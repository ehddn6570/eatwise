package domain.report.dto.response;

import domain.report.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {

    private Long reportId;
    private Long userId;
    private String username;
    private LocalDate reportDate;
    private Double caloriesConsumed;
    private String nutrientSummary;
    private String feedback;

    public static ReportResponse from(Report report) {
        return ReportResponse.builder()
                .reportId(report.getReportId())
                .userId(report.getUser().getUserId())
                .username(report.getUser().getUsername())
                .reportDate(report.getReportDate())
                .caloriesConsumed(report.getCaloriesConsumed())
                .nutrientSummary(report.getNutrientSummary())
                .feedback(report.getFeedback())
                .build();
    }
}
