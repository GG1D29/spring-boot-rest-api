package xie.stanley.restapiboot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int age;
}
