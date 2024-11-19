package xie.stanley.restapiboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int age;
}
