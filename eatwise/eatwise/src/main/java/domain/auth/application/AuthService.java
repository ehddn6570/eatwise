package domain.auth.application;

import domain.auth.dao.AuthRepository;
import domain.auth.domain.Auth;
import domain.auth.dto.request.AuthCreateRequest;
import domain.auth.dto.request.AuthVerifyRequest;
import domain.auth.dto.response.AuthResponse;
import domain.user.dao.UserRepository;
import domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    @Transactional
    public AuthResponse createAuth(AuthCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        Auth auth = Auth.builder()
                .user(user)
                .email(request.getEmail())
                .verificationCode(request.getVerificationCode())
                .expiresAt(request.getExpiresAt())
                .verified(request.getVerified() != null ? request.getVerified() : false)
                .build();

        Auth savedAuth = authRepository.save(auth);
        return AuthResponse.from(savedAuth);
    }

    @Transactional
    public AuthResponse sendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // Generate verification code
        String verificationCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10); // 10 minutes expiry

        Auth auth = Auth.builder()
                .user(user)
                .email(email)
                .verificationCode(verificationCode)
                .expiresAt(expiresAt)
                .verified(false)
                .build();

        Auth savedAuth = authRepository.save(auth);

        // TODO: Send email with verification code
        // emailService.sendVerificationEmail(email, verificationCode);

        return AuthResponse.from(savedAuth);
    }

    @Transactional
    public AuthResponse verifyCode(AuthVerifyRequest request) {
        Auth auth = authRepository.findByEmailAndVerificationCode(request.getEmail(), request.getVerificationCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification code"));

        if (auth.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification code has expired");
        }

        if (auth.getVerified()) {
            throw new IllegalArgumentException("Code already verified");
        }

        Auth verifiedAuth = Auth.builder()
                .authId(auth.getAuthId())
                .user(auth.getUser())
                .email(auth.getEmail())
                .verificationCode(auth.getVerificationCode())
                .expiresAt(auth.getExpiresAt())
                .verified(true)
                .build();

        Auth savedAuth = authRepository.save(verifiedAuth);
        return AuthResponse.from(savedAuth);
    }

    public AuthResponse getAuthById(Long authId) {
        Auth auth = authRepository.findById(authId)
                .orElseThrow(() -> new IllegalArgumentException("Auth not found with id: " + authId));
        return AuthResponse.from(auth);
    }

    public List<AuthResponse> getAuthsByUserId(Long userId) {
        return authRepository.findByUser_UserId(userId).stream()
                .map(AuthResponse::from)
                .collect(Collectors.toList());
    }

    public AuthResponse getLatestAuthByEmail(String email) {
        Auth auth = authRepository.findTopByEmailOrderByExpiresAtDesc(email)
                .orElseThrow(() -> new IllegalArgumentException("No auth found for email: " + email));
        return AuthResponse.from(auth);
    }

    @Transactional
    public void deleteAuth(Long authId) {
        if (!authRepository.existsById(authId)) {
            throw new IllegalArgumentException("Auth not found with id: " + authId);
        }
        authRepository.deleteById(authId);
    }
}
