package domain.auth.dto.response;

import domain.auth.domain.Auth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private Long authId;
    private Long userId;
    private String username;
    private String email;
    private String verificationCode;
    private LocalDateTime expiresAt;
    private Boolean verified;

    public static AuthResponse from(Auth auth) {
        return AuthResponse.builder()
                .authId(auth.getAuthId())
                .userId(auth.getUser().getUserId())
                .username(auth.getUser().getUsername())
                .email(auth.getEmail())
                .verificationCode(auth.getVerificationCode())
                .expiresAt(auth.getExpiresAt())
                .verified(auth.getVerified())
                .build();
    }
}
