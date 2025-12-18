package domain.food.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 음식명 사전 - OCR 인식 결과에서 음식명을 매칭하기 위한 사전
 * 예: '양념갈비' → '갈비', '계란말이' → '계란', '말이' 등으로 매칭
 */
@Entity
@Table(name = "food_dictionary")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dict_id")
    private Long dictId;

    @Column(name = "food_keyword", length = 100, nullable = false, unique = true)
    private String foodKeyword;

    @Column(name = "food_name", length = 100, nullable = false)
    private String foodName;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "description", length = 255)
    private String description;
}

