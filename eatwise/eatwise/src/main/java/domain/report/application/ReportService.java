package domain.report.application;

import domain.report.dao.ReportRepository;
import domain.report.domain.Report;
import domain.report.dto.request.ReportCreateRequest;
import domain.report.dto.request.ReportUpdateRequest;
import domain.report.dto.response.ReportResponse;
import domain.user.dao.UserRepository;
import domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReportResponse createReport(ReportCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        Report report = Report.builder()
                .user(user)
                .reportDate(request.getReportDate())
                .caloriesConsumed(request.getCaloriesConsumed())
                .nutrientSummary(request.getNutrientSummary())
                .feedback(request.getFeedback())
                .build();

        Report savedReport = reportRepository.save(report);
        return ReportResponse.from(savedReport);
    }

    public ReportResponse getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + reportId));
        return ReportResponse.from(report);
    }

    public List<ReportResponse> getReportsByUserId(Long userId) {
        return reportRepository.findByUser_UserIdOrderByReportDateDesc(userId).stream()
                .map(ReportResponse::from)
                .collect(Collectors.toList());
    }

    public ReportResponse getReportByUserIdAndDate(Long userId, LocalDate reportDate) {
        Report report = reportRepository.findByUser_UserIdAndReportDate(userId, reportDate)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Report not found for user id: " + userId + " and date: " + reportDate));
        return ReportResponse.from(report);
    }

    public List<ReportResponse> getReportsByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return reportRepository.findByUser_UserIdAndReportDateBetween(userId, startDate, endDate).stream()
                .map(ReportResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReportResponse updateReport(Long reportId, ReportUpdateRequest request) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with id: " + reportId));

        Report updatedReport = Report.builder()
                .reportId(report.getReportId())
                .user(report.getUser())
                .reportDate(request.getReportDate())
                .caloriesConsumed(request.getCaloriesConsumed())
                .nutrientSummary(request.getNutrientSummary())
                .feedback(request.getFeedback())
                .build();

        Report savedReport = reportRepository.save(updatedReport);
        return ReportResponse.from(savedReport);
    }

    @Transactional
    public void deleteReport(Long reportId) {
        if (!reportRepository.existsById(reportId)) {
            throw new IllegalArgumentException("Report not found with id: " + reportId);
        }
        reportRepository.deleteById(reportId);
    }
}
