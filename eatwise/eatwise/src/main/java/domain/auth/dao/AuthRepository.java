package domain.auth.dao;

import domain.auth.domain.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByEmail(String email);

    Optional<Auth> findByEmailAndVerificationCode(String email, String verificationCode);

    List<Auth> findByUser_UserId(Long userId);

    List<Auth> findByVerified(Boolean verified);

    Optional<Auth> findTopByEmailOrderByExpiresAtDesc(String email);
}
