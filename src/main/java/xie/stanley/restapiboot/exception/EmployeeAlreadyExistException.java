package xie.stanley.restapiboot.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmployeeAlreadyExistException extends RuntimeException {
    private final String email;

    @Override
    public String getMessage() {
        return "Employee already exists with this email: " + email;
    }
}
