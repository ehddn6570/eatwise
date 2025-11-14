package domain.notification.application;

import domain.notification.dao.NotificationRepository;
import domain.notification.domain.Notification;
import domain.notification.dto.request.NotificationCreateRequest;
import domain.notification.dto.request.NotificationUpdateRequest;
import domain.notification.dto.response.NotificationResponse;
import domain.user.dao.UserRepository;
import domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public NotificationResponse createNotification(NotificationCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        Notification notification = Notification.builder()
                .user(user)
                .type(request.getType())
                .message(request.getMessage())
                .createdAt(request.getCreatedAt())
                .readStatus(request.getReadStatus() != null ? request.getReadStatus() : false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return NotificationResponse.from(savedNotification);
    }

    public NotificationResponse getNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + notificationId));
        return NotificationResponse.from(notification);
    }

    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findByUser_UserIdAndReadStatus(userId, false).stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    public Long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByUser_UserIdAndReadStatus(userId, false);
    }

    @Transactional
    public NotificationResponse markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + notificationId));

        Notification updatedNotification = Notification.builder()
                .notificationId(notification.getNotificationId())
                .user(notification.getUser())
                .type(notification.getType())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .readStatus(true)
                .build();

        Notification savedNotification = notificationRepository.save(updatedNotification);
        return NotificationResponse.from(savedNotification);
    }

    @Transactional
    public void markAllAsReadByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUser_UserIdAndReadStatus(userId, false);

        List<Notification> updatedNotifications = notifications.stream()
                .map(notification -> Notification.builder()
                        .notificationId(notification.getNotificationId())
                        .user(notification.getUser())
                        .type(notification.getType())
                        .message(notification.getMessage())
                        .createdAt(notification.getCreatedAt())
                        .readStatus(true)
                        .build())
                .collect(Collectors.toList());

        notificationRepository.saveAll(updatedNotifications);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new IllegalArgumentException("Notification not found with id: " + notificationId);
        }
        notificationRepository.deleteById(notificationId);
    }
}
