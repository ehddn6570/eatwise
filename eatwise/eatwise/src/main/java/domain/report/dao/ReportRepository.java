package domain.report.dao;

import domain.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUser_UserId(Long userId);

    List<Report> findByUser_UserIdOrderByReportDateDesc(Long userId);

    Optional<Report> findByUser_UserIdAndReportDate(Long userId, LocalDate reportDate);

    List<Report> findByUser_UserIdAndReportDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
