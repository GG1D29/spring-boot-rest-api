package xie.stanley.restapiboot.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {
	private int userId;

	@Override
	public String getMessage() {
		return "user with id: " + userId + " is not found";
	}
}
