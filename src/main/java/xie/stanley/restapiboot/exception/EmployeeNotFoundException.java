package xie.stanley.restapiboot.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmployeeNotFoundException extends RuntimeException {
    private final String email;

    @Override
    public String getMessage() {
        return "Employee not found: " + email;
    }
}
