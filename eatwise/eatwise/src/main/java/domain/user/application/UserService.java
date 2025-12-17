package domain.user.application;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.user.dao.UserRepository;
import domain.user.domain.User;
import domain.user.dto.request.UserLoginRequest;
import domain.user.dto.request.UserSignupRequest;
import domain.user.dto.request.UserUpdateRequest;
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

	public UserResponse getUserById(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

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

	@Transactional
	public UserResponse updateUser(Long userId, UserUpdateRequest request) {
		System.out.println("🔍 사용자 정보 수정 시작: userId=" + userId);
		System.out.println("📝 요청 정보: " + request);

		try {
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

			System.out.println("✅ 기존 사용자 조회 성공");

			// 비밀번호 업데이트
			if (request.getPassword() != null && !request.getPassword().isEmpty()) {
				user.setPassword(passwordEncoder.encode(request.getPassword()));
				System.out.println("✅ 비밀번호 업데이트");
			}

			// 나이, 키, 몸무게 업데이트
			if (request.getAge() != null) {
				user.setAge(request.getAge());
				System.out.println("✅ 나이 업데이트: " + request.getAge());
			}
			if (request.getHeight() != null) {
				user.setHeight(request.getHeight());
				System.out.println("✅ 키 업데이트: " + request.getHeight());
			}
			if (request.getWeight() != null) {
				user.setWeight(request.getWeight());
				System.out.println("✅ 몸무게 업데이트: " + request.getWeight());
			}

			// @Transactional이 있으므로 자동 저장됨
			System.out.println("✅ 사용자 정보 저장");

			return UserResponse.builder()
				.userId(user.getUserId())
				.username(user.getUsername())
				.email(user.getEmail())
				.age(user.getAge())
				.gender(user.getGender())
				.height(user.getHeight())
				.weight(user.getWeight())
				.build();
		} catch (Exception e) {
			System.err.println("❌ 사용자 정보 수정 중 오류: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("사용자 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	public boolean checkEmailExists(String email) {
		return userRepository.existsByEmail(email);
	}
}
