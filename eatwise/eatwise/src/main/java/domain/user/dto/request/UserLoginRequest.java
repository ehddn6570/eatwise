package domain.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserLoginRequest {
	private String email;
	private String password;
}
