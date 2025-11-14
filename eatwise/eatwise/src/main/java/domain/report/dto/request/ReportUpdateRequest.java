package domain.report.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportUpdateRequest {

    private LocalDate reportDate;
    private Double caloriesConsumed;
    private String nutrientSummary;
    private String feedback;
}
