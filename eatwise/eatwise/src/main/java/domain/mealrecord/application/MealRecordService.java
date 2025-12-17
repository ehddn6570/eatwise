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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        // 유효성 검증
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("섭취량은 0보다 커야 합니다");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + request.getFoodId()));

        // totalCalories 자동 계산 (음식의 칼로리 * 섭취량 / 100)
        Double totalCalories = (food.getCalories() * request.getQuantity()) / 100.0;

        // String intakeDate를 LocalDate로 변환 ("yyyy-MM-dd" → LocalDate)
        LocalDate intakeDate = null;
        if (request.getIntakeDate() != null && !request.getIntakeDate().isEmpty()) {
            try {
                intakeDate = LocalDate.parse(request.getIntakeDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format. Expected: yyyy-MM-dd");
            }
        }

        // String intakeTime을 LocalDateTime으로 변환 ("HH:mm:ss" → LocalDateTime)
        LocalDateTime intakeTime = null;
        if (request.getIntakeTime() != null && !request.getIntakeTime().isEmpty()) {
            try {
                LocalTime time = LocalTime.parse(request.getIntakeTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
                intakeTime = LocalDateTime.of(intakeDate, time);
            } catch (Exception e) {
                // 변환 실패 시 null로 설정
                intakeTime = null;
            }
        }

        MealRecord mealRecord = MealRecord.builder()
                .user(user)
                .food(food)
                .mealType(request.getMealType())
                .intakeTime(intakeTime)
                .intakeDate(intakeDate)
                .quantity(request.getQuantity())
                .totalCalories(totalCalories)
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

        // totalCalories 자동 계산
        Double totalCalories = (mealRecord.getFood().getCalories() * request.getQuantity()) / 100.0;

        // String intakeDate를 LocalDate로 변환 ("yyyy-MM-dd" → LocalDate)
        LocalDate intakeDate = null;
        if (request.getIntakeDate() != null && !request.getIntakeDate().isEmpty()) {
            try {
                intakeDate = LocalDate.parse(request.getIntakeDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format. Expected: yyyy-MM-dd");
            }
        }

        // String intakeTime을 LocalDateTime으로 변환 ("HH:mm:ss" → LocalDateTime)
        LocalDateTime intakeTime = null;
        if (request.getIntakeTime() != null && !request.getIntakeTime().isEmpty()) {
            try {
                LocalTime time = LocalTime.parse(request.getIntakeTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));
                intakeTime = LocalDateTime.of(intakeDate, time);
            } catch (Exception e) {
                // 변환 실패 시 null로 설정
                intakeTime = null;
            }
        }

        MealRecord updatedMealRecord = MealRecord.builder()
                .recordId(mealRecord.getRecordId())
                .user(mealRecord.getUser())
                .food(mealRecord.getFood())
                .mealType(request.getMealType())
                .intakeTime(intakeTime)
                .intakeDate(intakeDate)
                .quantity(request.getQuantity())
                .totalCalories(totalCalories)
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
