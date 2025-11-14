package domain.user.application;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import domain.user.dao.UserRepository;
import domain.user.domain.User;
import domain.user.dto.request.UserLoginRequest;
import domain.user.dto.request.UserSignupRequest;
import domain.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserResponse signup(UserSignupRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("이미 존재하는 이메일입니다.");
		}

		User user = User.builder()
			.username(request.getUsername())
			.password(passwordEncoder.encode(request.getPassword()))
			.email(request.getEmail())
			.age(request.getAge())
			.gender(request.getGender())
			.height(request.getHeight())
			.weight(request.getWeight())
			.createdAt(LocalDateTime.now())
			.build();

		User saved = userRepository.save(user);

		return UserResponse.builder()
			.userId(saved.getUserId())
			.username(saved.getUsername())
			.email(saved.getEmail())
			.age(saved.getAge())
			.gender(saved.getGender())
			.height(saved.getHeight())
			.weight(saved.getWeight())
			.build();
	}


	public UserResponse login(UserLoginRequest request) {

		User user = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}

		return UserResponse.builder()
			.userId(user.getUserId())
			.username(user.getUsername())
			.email(user.getEmail())
			.age(user.getAge())
			.gender(user.getGender())
			.height(user.getHeight())
			.weight(user.getWeight())
			.build();
	}
}
