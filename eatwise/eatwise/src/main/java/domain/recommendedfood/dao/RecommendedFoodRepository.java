package domain.recommendedfood.dao;

import domain.recommendedfood.domain.RecommendedFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommendedFoodRepository extends JpaRepository<RecommendedFood, Long> {

    List<RecommendedFood> findByUser_UserId(Long userId);

    List<RecommendedFood> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    List<RecommendedFood> findByUser_UserIdAndCreatedAtAfter(Long userId, LocalDateTime dateTime);

    List<RecommendedFood> findByFood_FoodId(Long foodId);
}
