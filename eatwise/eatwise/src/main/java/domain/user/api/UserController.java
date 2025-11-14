package domain.user.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.user.application.UserService;
import domain.user.dto.request.UserLoginRequest;
import domain.user.dto.request.UserSignupRequest;
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
}
