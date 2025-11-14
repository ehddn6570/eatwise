package domain.goal.dao;

import domain.goal.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUser_UserId(Long userId);

    Optional<Goal> findByUser_UserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long userId, LocalDate date1, LocalDate date2);

    List<Goal> findByUser_UserIdAndEndDateAfter(Long userId, LocalDate date);
}
