package domain.mealrecord.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MealRecordCreateRequest {

    private Long userId;
    private Long foodId;
    private String mealType;

    private String intakeTime;      // "HH:mm:ss" 형식의 문자열
    private String intakeDate;      // "yyyy-MM-dd" 형식의 문자열

    private Double quantity;
}
