package domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthCreateRequest {

    private Long userId;
    private String email;
    private String verificationCode;
    private LocalDateTime expiresAt;
    private Boolean verified;
}
