package xie.stanley.restapiboot.dto;

public class EmployeeDtoBuilder {
    private String firstName;
    private String lastName;
    private String email;

    private EmployeeDtoBuilder() {
    }

    public static EmployeeDtoBuilder anEmployeeDto() {
        return new EmployeeDtoBuilder();
    }

    public EmployeeDtoBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public EmployeeDtoBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public EmployeeDtoBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public EmployeeDto build() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName(firstName);
        employeeDto.setLastName(lastName);
        employeeDto.setEmail(email);

        return employeeDto;
    }
}
