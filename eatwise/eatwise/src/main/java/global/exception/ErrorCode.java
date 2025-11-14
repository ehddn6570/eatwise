package global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C001", "내부 서버 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C002", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "허용되지 않은 메서드입니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C004", "엔티티를 찾을 수 없습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C005", "잘못된 타입입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C006", "접근이 거부되었습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "이미 존재하는 이메일입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "U003", "이미 존재하는 사용자명입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U004", "비밀번호가 일치하지 않습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "U005", "이메일 또는 비밀번호가 올바르지 않습니다."),

    // Food
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "F001", "음식을 찾을 수 없습니다."),

    // MealRecord
    MEAL_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "식사 기록을 찾을 수 없습니다."),

    // Goal
    GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "G001", "목표를 찾을 수 없습니다."),
    ACTIVE_GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "G002", "활성화된 목표를 찾을 수 없습니다."),

    // Notification
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "알림을 찾을 수 없습니다."),

    // Restaurant
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "식당을 찾을 수 없습니다."),

    // RecommendedFood
    RECOMMENDED_FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "RF001", "추천 음식을 찾을 수 없습니다."),

    // Report
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "RP001", "리포트를 찾을 수 없습니다."),

    // Auth
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "인증 정보를 찾을 수 없습니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "A002", "인증 코드가 만료되었습니다."),
    VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "A003", "인증 코드가 일치하지 않습니다."),
    ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "A004", "이미 인증되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
