package xie.stanley.restapiboot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {
    private String firstName;
    private String lastName;
    @NotEmpty(message = "Email may not be empty")
    private String email;
    private String phone;
    private int age;
}
