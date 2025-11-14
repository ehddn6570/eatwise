package domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserResponse {
	private Long userId;
	private String username;
	private String email;
	private Integer age;
	private String gender;
	private Double height;
	private Double weight;
}
