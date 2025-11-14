package domain.mealrecord.application;

import domain.food.dao.FoodRepository;
import domain.food.domain.Food;
import domain.mealrecord.dao.MealRecordRepository;
import domain.mealrecord.domain.MealRecord;
import domain.mealrecord.dto.request.MealRecordCreateRequest;
import domain.mealrecord.dto.request.MealRecordUpdateRequest;
import domain.mealrecord.dto.response.MealRecordResponse;
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
public class MealRecordService {

    private final MealRecordRepository mealRecordRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    @Transactional
    public MealRecordResponse createMealRecord(MealRecordCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + request.getFoodId()));

        MealRecord mealRecord = MealRecord.builder()
                .user(user)
                .food(food)
                .intakeTime(request.getIntakeTime())
                .intakeDate(request.getIntakeDate())
                .quantity(request.getQuantity())
                .totalCalories(request.getTotalCalories())
                .build();

        MealRecord savedMealRecord = mealRecordRepository.save(mealRecord);
        return MealRecordResponse.from(savedMealRecord);
    }

    public MealRecordResponse getMealRecordById(Long recordId) {
        MealRecord mealRecord = mealRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("MealRecord not found with id: " + recordId));
        return MealRecordResponse.from(mealRecord);
    }

    public List<MealRecordResponse> getMealRecordsByUserId(Long userId) {
        return mealRecordRepository.findByUser_UserId(userId).stream()
                .map(MealRecordResponse::from)
                .collect(Collectors.toList());
    }

    public List<MealRecordResponse> getMealRecordsByUserIdAndDate(Long userId, LocalDate date) {
        return mealRecordRepository.findByUser_UserIdAndIntakeDate(userId, date).stream()
                .map(MealRecordResponse::from)
                .collect(Collectors.toList());
    }

    public List<MealRecordResponse> getMealRecordsByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return mealRecordRepository.findByUser_UserIdAndIntakeDateBetween(userId, startDate, endDate).stream()
                .map(MealRecordResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public MealRecordResponse updateMealRecord(Long recordId, MealRecordUpdateRequest request) {
        MealRecord mealRecord = mealRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("MealRecord not found with id: " + recordId));

        MealRecord updatedMealRecord = MealRecord.builder()
                .recordId(mealRecord.getRecordId())
                .user(mealRecord.getUser())
                .food(mealRecord.getFood())
                .intakeTime(request.getIntakeTime())
                .intakeDate(request.getIntakeDate())
                .quantity(request.getQuantity())
                .totalCalories(request.getTotalCalories())
                .build();

        MealRecord savedMealRecord = mealRecordRepository.save(updatedMealRecord);
        return MealRecordResponse.from(savedMealRecord);
    }

    @Transactional
    public void deleteMealRecord(Long recordId) {
        if (!mealRecordRepository.existsById(recordId)) {
            throw new IllegalArgumentException("MealRecord not found with id: " + recordId);
        }
        mealRecordRepository.deleteById(recordId);
    }
}
