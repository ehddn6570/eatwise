package domain.user.api;

import java.util.Map;
import org.springframework.web.bind.annotation.*;

import domain.user.application.UserService;
import domain.user.dto.request.UserLoginRequest;
import domain.user.dto.request.UserSignupRequest;
import domain.user.dto.request.UserUpdateRequest;
import domain.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public UserResponse signup(@RequestBody UserSignupRequest request) {
		return userService.signup(request);
	}

	@PostMapping("/login")
	public UserResponse login(@RequestBody UserLoginRequest request) {
		return userService.login(request);
	}

	@PutMapping("/{userId}")
	public UserResponse updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
		return userService.updateUser(userId, request);
	}

	@GetMapping("/{userId}")
	public UserResponse getUser(@PathVariable Long userId) {
		return userService.getUserById(userId);
	}

	@GetMapping("/check-email/{email}")
	public Map<String, Object> checkEmail(@PathVariable String email) {
		boolean exists = userService.checkEmailExists(email);
		return Map.of("exists", exists);
	}
}
