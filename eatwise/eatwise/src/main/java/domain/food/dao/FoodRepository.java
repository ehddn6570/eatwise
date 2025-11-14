package domain.food.dao;

import domain.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    Optional<Food> findByFoodName(String foodName);

    List<Food> findByCategory(String category);

    List<Food> findByFoodNameContaining(String keyword);

    List<Food> findByCaloriesBetween(Double minCalories, Double maxCalories);
}
