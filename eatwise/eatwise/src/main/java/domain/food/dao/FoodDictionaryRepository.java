package domain.food.dao;

import domain.food.domain.FoodDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodDictionaryRepository extends JpaRepository<FoodDictionary, Long> {

    /**
     * 정확한 키워드로 검색
     */
    Optional<FoodDictionary> findByFoodKeyword(String foodKeyword);

    /**
     * 키워드 포함 검색
     */
    List<FoodDictionary> findByFoodKeywordContaining(String keyword);

    /**
     * 음식명으로 검색
     */
    List<FoodDictionary> findByFoodNameContaining(String foodName);

    /**
     * 카테고리로 검색
     */
    List<FoodDictionary> findByCategory(String category);

    /**
     * 키워드가 포함된 모든 엔트리 검색 (OCR 결과와 비교용)
     */
    @Query("SELECT fd FROM FoodDictionary fd WHERE :text LIKE CONCAT('%', fd.foodKeyword, '%')")
    List<FoodDictionary> findAllByKeywordInText(@Param("text") String text);
}

