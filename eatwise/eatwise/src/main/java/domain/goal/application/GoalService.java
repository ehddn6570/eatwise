package domain.goal.application;

import domain.goal.dao.GoalRepository;
import domain.goal.domain.Goal;
import domain.goal.dto.request.GoalCreateRequest;
import domain.goal.dto.request.GoalUpdateRequest;
import domain.goal.dto.response.GoalResponse;
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
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Transactional
    public GoalResponse createGoal(GoalCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        // 기본 칼로리 (사용자 정보 기반으로 계산된 TDEE)
        double baseTdee = calculateTDEE(user);

        // 목표 타입에 따라 칼로리 조정
        Double dailyCalorieTarget = calculateCalorieByGoalType(baseTdee, request.getGoalType());

        // 목표 칼로리에 따라 영양소 목표 계산
        double carbRatio = request.getCarbRatio() != null ? request.getCarbRatio() : 0.5;
        double proteinRatio = request.getProteinRatio() != null ? request.getProteinRatio() : 0.25;
        double fatRatio = request.getFatRatio() != null ? request.getFatRatio() : 0.25;

        Goal goal = Goal.builder()
                .user(user)
                .goalType(request.getGoalType())
                .dailyCalorieTarget(dailyCalorieTarget)
                .carbRatio(carbRatio)
                .proteinRatio(proteinRatio)
                .fatRatio(fatRatio)
                .startDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now())
                .endDate(request.getEndDate())
                .build();

        Goal savedGoal = goalRepository.save(goal);
        return GoalResponse.from(savedGoal);
    }

    // TDEE 계산 (Harris-Benedict 공식)
    private double calculateTDEE(User user) {
        double bmr;
        if ("M".equalsIgnoreCase(user.getGender())) {
            bmr = 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * (user.getAge() != null ? user.getAge() : 30));
        } else {
            bmr = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * (user.getAge() != null ? user.getAge() : 30));
        }
        return bmr * 1.5;  // 활동량 계수 1.5 (보통 활동)
    }

    // 목표 타입에 따라 칼로리 조정
    private Double calculateCalorieByGoalType(double baseTdee, Goal.GoalType goalType) {
        if (goalType == null) return baseTdee;

        switch (goalType) {
            case GAIN:
                return baseTdee + 300;  // 체중 증량: +300 kcal
            case LOSE:
                return baseTdee - 300;  // 체중 감량: -300 kcal
            case MAINTAIN:
            default:
                return baseTdee;        // 체중 유지: 그대로
        }
    }

    public GoalResponse getGoalById(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found with id: " + goalId));
        return GoalResponse.from(goal);
    }

    public List<GoalResponse> getGoalsByUserId(Long userId) {
        return goalRepository.findByUser_UserId(userId).stream()
                .map(GoalResponse::from)
                .collect(Collectors.toList());
    }

    public GoalResponse getCurrentGoalByUserId(Long userId) {
        LocalDate today = LocalDate.now();
        Goal goal = goalRepository.findByUser_UserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        userId, today, today)
                .orElseThrow(() -> new IllegalArgumentException("No active goal found for user id: " + userId));
        return GoalResponse.from(goal);
    }

    public List<GoalResponse> getActiveGoalsByUserId(Long userId) {
        LocalDate today = LocalDate.now();
        return goalRepository.findByUser_UserIdAndEndDateAfter(userId, today).stream()
                .map(GoalResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public GoalResponse updateGoal(Long goalId, GoalUpdateRequest request) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found with id: " + goalId));

        // 기본 칼로리 재계산
        double baseTdee = calculateTDEE(goal.getUser());
        Double dailyCalorieTarget = calculateCalorieByGoalType(baseTdee, request.getGoalType());

        Goal updatedGoal = Goal.builder()
                .goalId(goal.getGoalId())
                .user(goal.getUser())
                .goalType(request.getGoalType() != null ? request.getGoalType() : goal.getGoalType())
                .dailyCalorieTarget(dailyCalorieTarget)
                .carbRatio(request.getCarbRatio() != null ? request.getCarbRatio() : goal.getCarbRatio())
                .proteinRatio(request.getProteinRatio() != null ? request.getProteinRatio() : goal.getProteinRatio())
                .fatRatio(request.getFatRatio() != null ? request.getFatRatio() : goal.getFatRatio())
                .startDate(request.getStartDate() != null ? request.getStartDate() : goal.getStartDate())
                .endDate(request.getEndDate() != null ? request.getEndDate() : goal.getEndDate())
                .build();

        Goal savedGoal = goalRepository.save(updatedGoal);
        return GoalResponse.from(savedGoal);
    }

    @Transactional
    public void deleteGoal(Long goalId) {
        if (!goalRepository.existsById(goalId)) {
            throw new IllegalArgumentException("Goal not found with id: " + goalId);
        }
        goalRepository.deleteById(goalId);
    }
}
