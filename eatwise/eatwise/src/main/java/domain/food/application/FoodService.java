package domain.food.application;

import domain.food.dao.FoodRepository;
import domain.food.dao.FoodDictionaryRepository;
import domain.food.domain.Food;
import domain.food.domain.FoodDictionary;
import domain.food.dto.request.FoodCreateRequest;
import domain.food.dto.request.FoodUpdateRequest;
import domain.food.dto.response.FoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodDictionaryRepository foodDictionaryRepository;

    @Transactional
    public FoodResponse createFood(FoodCreateRequest request) {
        Food food = Food.builder()
                .foodName(request.getFoodName())
                .category(request.getCategory())
                .calories(request.getCalories())
                .carbs(request.getCarbs())
                .protein(request.getProtein())
                .fat(request.getFat())
                .imageUrl(request.getImageUrl())
                .build();

        Food savedFood = foodRepository.save(food);
        return FoodResponse.from(savedFood);
    }

    public FoodResponse getFoodById(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + foodId));
        return FoodResponse.from(food);
    }

    public List<FoodResponse> getAllFoods() {
        return foodRepository.findAll().stream()
                .map(FoodResponse::from)
                .collect(Collectors.toList());
    }

    public List<FoodResponse> getFoodsByCategory(String category) {
        return foodRepository.findByCategory(category).stream()
                .map(FoodResponse::from)
                .collect(Collectors.toList());
    }

    public List<FoodResponse> searchFoodsByName(String keyword) {
        // DB에서 검색
        List<FoodResponse> dbResults = foodRepository.searchByKeyword(keyword).stream()
                .map(FoodResponse::from)
                .collect(Collectors.toList());

        // Java에서 정확한 포함 확인
        List<FoodResponse> filteredResults = dbResults.stream()
                .filter(food -> food.getFoodName().contains(keyword))
                .collect(Collectors.toList());

        // 중복 제거
        java.util.LinkedHashMap<String, FoodResponse> uniqueMap = new java.util.LinkedHashMap<>();
        for (FoodResponse food : filteredResults) {
            uniqueMap.putIfAbsent(food.getFoodName(), food);
        }

        List<FoodResponse> uniqueResults = new java.util.ArrayList<>(uniqueMap.values());


        return uniqueResults;
    }


    // OCR 텍스트에서 음식명 추출 (음식 사전 기반 부분 매칭)
    public List<FoodResponse> extractFoodsFromOCRText(String ocrText) {
        List<FoodResponse> results = new java.util.ArrayList<>();
        java.util.Set<Long> addedFoodIds = new java.util.HashSet<>();

        try {
            // 1단계: 음식 사전에서 모든 키워드 가져오기
            List<FoodDictionary> allDictionaries = foodDictionaryRepository.findAll();

            // 2단계: OCR 텍스트에서 각 키워드 검색 (부분 매칭)
            for (FoodDictionary dictionary : allDictionaries) {
                String keyword = dictionary.getFoodKeyword();

                // OCR 텍스트에 키워드가 포함되어 있는지 확인 (대소문자 무시)
                if (ocrText.toLowerCase().contains(keyword.toLowerCase())) {
                    System.out.println("✓ 매칭된 키워드: " + keyword + " → " + dictionary.getFoodName());

                    // 해당 음식명으로 Food 데이터베이스에서 검색
                    List<Food> foods = foodRepository.findByFoodNameContaining(dictionary.getFoodName());

                    for (Food food : foods) {
                        if (!addedFoodIds.contains(food.getFoodId())) {
                            results.add(FoodResponse.from(food));
                            addedFoodIds.add(food.getFoodId());
                        }
                    }
                }
            }

            System.out.println("📝 추출된 음식 수: " + results.size());
            if (results.isEmpty()) {
                System.out.println("❌ 매칭된 음식이 없습니다.");
            }

        } catch (Exception e) {
            System.err.println("음식명 추출 실패: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }


    @Transactional
    public FoodResponse updateFood(Long foodId, FoodUpdateRequest request) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + foodId));

        Food updatedFood = Food.builder()
                .foodId(food.getFoodId())
                .foodName(request.getFoodName())
                .category(request.getCategory())
                .calories(request.getCalories())
                .carbs(request.getCarbs())
                .protein(request.getProtein())
                .fat(request.getFat())
                .imageUrl(request.getImageUrl())
                .build();

        Food savedFood = foodRepository.save(updatedFood);
        return FoodResponse.from(savedFood);
    }

    @Transactional
    public void deleteFood(Long foodId) {
        if (!foodRepository.existsById(foodId)) {
            throw new IllegalArgumentException("Food not found with id: " + foodId);
        }
        foodRepository.deleteById(foodId);
    }

    // ==================== 음식 사전 관리 메서드 ====================

    /**
     * 음식 사전에 새로운 키워드 추가
     * 예: "양념갈비" 키워드로 "갈비" 음식명을 등록하면,
     * OCR에서 "양념갈비"를 인식했을 때 자동으로 "갈비" 음식으로 매칭됨
     */
    @Transactional
    public void addFoodDictionary(String foodKeyword, String foodName, String category, String description) {
        FoodDictionary dictionary = FoodDictionary.builder()
                .foodKeyword(foodKeyword)
                .foodName(foodName)
                .category(category)
                .description(description)
                .build();

        foodDictionaryRepository.save(dictionary);
        System.out.println("✓ 음식 사전 추가: '" + foodKeyword + "' → '" + foodName + "'");
    }

    /**
     * 음식 사전 검색
     */
    public List<FoodDictionary> searchFoodDictionary(String keyword) {
        return foodDictionaryRepository.findByFoodKeywordContaining(keyword);
    }

    /**
     * 모든 음식 사전 조회
     */
    public List<FoodDictionary> getAllFoodDictionaries() {
        return foodDictionaryRepository.findAll();
    }

    /**
     * 음식 사전 삭제
     */
    @Transactional
    public void deleteFoodDictionary(Long dictId) {
        if (!foodDictionaryRepository.existsById(dictId)) {
            throw new IllegalArgumentException("Dictionary entry not found with id: " + dictId);
        }
        foodDictionaryRepository.deleteById(dictId);
    }
}

