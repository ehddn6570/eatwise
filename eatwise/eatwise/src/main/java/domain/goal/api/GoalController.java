package domain.goal.api;

import domain.goal.application.GoalService;
import domain.goal.dto.request.GoalCreateRequest;
import domain.goal.dto.request.GoalUpdateRequest;
import domain.goal.dto.response.GoalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(@RequestBody GoalCreateRequest request) {
        GoalResponse response = goalService.createGoal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<GoalResponse> getGoalById(@PathVariable Long goalId) {
        GoalResponse response = goalService.getGoalById(goalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GoalResponse>> getGoalsByUserId(@PathVariable Long userId) {
        List<GoalResponse> responses = goalService.getGoalsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/current")
    public ResponseEntity<GoalResponse> getCurrentGoalByUserId(@PathVariable Long userId) {
        GoalResponse response = goalService.getCurrentGoalByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<GoalResponse>> getActiveGoalsByUserId(@PathVariable Long userId) {
        List<GoalResponse> responses = goalService.getActiveGoalsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<GoalResponse> updateGoal(
            @PathVariable Long goalId,
            @RequestBody GoalUpdateRequest request) {
        GoalResponse response = goalService.updateGoal(goalId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.noContent().build();
    }
}
