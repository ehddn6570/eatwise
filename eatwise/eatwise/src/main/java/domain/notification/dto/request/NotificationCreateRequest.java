package domain.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateRequest {

    private Long userId;
    private String type;
    private String message;
    private LocalDateTime createdAt;
    private Boolean readStatus;
}
