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

        Goal goal = Goal.builder()
                .user(user)
                .dailyCalorieTarget(request.getDailyCalorieTarget())
                .carbRatio(request.getCarbRatio())
                .proteinRatio(request.getProteinRatio())
                .fatRatio(request.getFatRatio())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Goal savedGoal = goalRepository.save(goal);
        return GoalResponse.from(savedGoal);
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

        Goal updatedGoal = Goal.builder()
                .goalId(goal.getGoalId())
                .user(goal.getUser())
                .dailyCalorieTarget(request.getDailyCalorieTarget())
                .carbRatio(request.getCarbRatio())
                .proteinRatio(request.getProteinRatio())
                .fatRatio(request.getFatRatio())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
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
