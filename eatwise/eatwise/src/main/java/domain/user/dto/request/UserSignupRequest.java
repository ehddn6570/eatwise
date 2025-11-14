package domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class UserSignupRequest {

	private String username;
	private String password;
	private String email;
	private Integer age;
	private String gender;
	private Double height;
	private Double weight;


}

