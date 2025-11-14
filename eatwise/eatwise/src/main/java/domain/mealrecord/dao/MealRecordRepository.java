package domain.mealrecord.dao;

import domain.mealrecord.domain.MealRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRecordRepository extends JpaRepository<MealRecord, Long> {

    List<MealRecord> findByUser_UserId(Long userId);

    List<MealRecord> findByUser_UserIdAndIntakeDate(Long userId, LocalDate intakeDate);

    List<MealRecord> findByUser_UserIdAndIntakeDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<MealRecord> findByIntakeDate(LocalDate intakeDate);
}
