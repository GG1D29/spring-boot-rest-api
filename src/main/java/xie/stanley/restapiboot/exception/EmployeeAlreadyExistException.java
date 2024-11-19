package xie.stanley.restapiboot.exception;

public class EmployeeAlreadyExistException extends RuntimeException {
    private String email;

    public EmployeeAlreadyExistException(String email) {
        this.email = email;
    }

    @Override
    public String getMessage() {
        return "Employee already exists with this email: " + email;
    }
}
