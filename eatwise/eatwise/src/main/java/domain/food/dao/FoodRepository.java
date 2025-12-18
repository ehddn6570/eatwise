package domain.food.dao;

import domain.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    Optional<Food> findByFoodName(String foodName);

    List<Food> findByCategory(String category);

    List<Food> findByFoodNameContaining(String keyword);

    List<Food> findByCaloriesBetween(Double minCalories, Double maxCalories);

    // 정확한 포함 검사 (중복 제거)
    @Query("SELECT DISTINCT f FROM Food f WHERE f.foodName LIKE %:keyword%")
    List<Food> searchByKeyword(@Param("keyword") String keyword);
}


