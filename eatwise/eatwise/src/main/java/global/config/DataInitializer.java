package global.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import domain.user.dao.UserRepository;
import domain.user.domain.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initTestUser() {
        return args -> {
            // 테스트 계정이 이미 존재하는지 확인
            if (!userRepository.existsByEmail("1111@1111")) {
                User testUser = User.builder()
                        .username("테스트유저")
                        .email("1111@1111")
                        .password(passwordEncoder.encode("1111"))
                        .age(25)
                        .gender("male")
                        .height(175.0)
                        .weight(70.0)
                        .createdAt(LocalDateTime.now())
                        .build();

                userRepository.save(testUser);
                System.out.println("========================================");
                System.out.println("테스트 계정이 생성되었습니다!");
                System.out.println("이메일: 1111@1111");
                System.out.println("비밀번호: 1111");
                System.out.println("⚠️  나중에 DataInitializer.java 파일을 삭제하세요!");
                System.out.println("========================================");
            }
        };
    }
}
