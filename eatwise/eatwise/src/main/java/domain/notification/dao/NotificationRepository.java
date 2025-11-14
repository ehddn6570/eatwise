package domain.notification.dao;

import domain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser_UserId(Long userId);

    List<Notification> findByUser_UserIdAndReadStatus(Long userId, Boolean readStatus);

    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    Long countByUser_UserIdAndReadStatus(Long userId, Boolean readStatus);
}
