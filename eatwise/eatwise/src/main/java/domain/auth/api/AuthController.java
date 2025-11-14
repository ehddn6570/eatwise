package domain.auth.api;

import domain.auth.application.AuthService;
import domain.auth.dto.request.AuthCreateRequest;
import domain.auth.dto.request.AuthVerifyRequest;
import domain.auth.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponse> createAuth(@RequestBody AuthCreateRequest request) {
        AuthResponse response = authService.createAuth(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/send-code")
    public ResponseEntity<AuthResponse> sendVerificationCode(@RequestParam String email) {
        AuthResponse response = authService.sendVerificationCode(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyCode(@RequestBody AuthVerifyRequest request) {
        AuthResponse response = authService.verifyCode(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{authId}")
    public ResponseEntity<AuthResponse> getAuthById(@PathVariable Long authId) {
        AuthResponse response = authService.getAuthById(authId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuthResponse>> getAuthsByUserId(@PathVariable Long userId) {
        List<AuthResponse> responses = authService.getAuthsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/email/{email}/latest")
    public ResponseEntity<AuthResponse> getLatestAuthByEmail(@PathVariable String email) {
        AuthResponse response = authService.getLatestAuthByEmail(email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{authId}")
    public ResponseEntity<Void> deleteAuth(@PathVariable Long authId) {
        authService.deleteAuth(authId);
        return ResponseEntity.noContent().build();
    }
}
