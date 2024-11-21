package xie.stanley.restapiboot.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import xie.stanley.restapiboot.dto.EmployeeDto;
import xie.stanley.restapiboot.dto.EmployeeDtoBuilder;
import xie.stanley.restapiboot.entity.Employee;
import xie.stanley.restapiboot.exception.EmployeeAlreadyExistException;
import xie.stanley.restapiboot.exception.EmployeeNotFoundException;
import xie.stanley.restapiboot.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class EmployeeServiceIT {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void should_AddEmployee_Successfully() {
        employeeService.addEmployee(getCreateEmployeeDTO());

        EmployeeDto employee = employeeService.getEmployee("john@doe.com");
        assertThat(employee.getFirstName()).isEqualTo("John");
        assertThat(employee.getLastName()).isEqualTo("Doe");
        assertThat(employee.getEmail()).isEqualTo("john@doe.com");

        // Checking the ID saved is valid.
        Optional<Employee> savedEmployee = employeeRepository.findByEmail("john@doe.com");
        assertThat(savedEmployee).isPresent();
        assertThat(savedEmployee.get().getId()).isNotNull();
        assertThat(savedEmployee.get().getId()).isGreaterThan(0);
    }


    @Test
    void should_Failed_AddEmployee_When_EmailAlreadyExist() {
        employeeService.addEmployee(getCreateEmployeeDTO());

        assertThrows(EmployeeAlreadyExistException.class, () -> employeeService.addEmployee(getCreateEmployeeDTO()));
    }

    private EmployeeDto getCreateEmployeeDTO() {
        return EmployeeDtoBuilder.anEmployeeDto()
                .withFirstName("John")
                .withLastName("Doe")
                .withEmail("john@doe.com")
                .build();
    }

    @Test
    void should_Failed_ReturnEmployeeByEmail_When_EmailDoesNotExist() {
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployee("john@doe.com"));
    }

    @Test
    void should_GetAllEmployees_Successfully() {
        EmployeeDto employee1 = EmployeeDtoBuilder.anEmployeeDto().withEmail("john@doe.com").build();
        EmployeeDto employee2 = EmployeeDtoBuilder.anEmployeeDto().withEmail("doe@john.com").build();
        EmployeeDto employee3 = EmployeeDtoBuilder.anEmployeeDto().withEmail("bubu@dudu.com").build();

        employeeService.addEmployee(employee1);
        employeeService.addEmployee(employee2);
        employeeService.addEmployee(employee3);

        List<EmployeeDto> actual = employeeService.getAllEmployees();
        assertThat(actual).hasSize(3);
        assertThat(actual.get(0).getEmail()).isEqualTo("john@doe.com");
        assertThat(actual.get(1).getEmail()).isEqualTo("doe@john.com");
        assertThat(actual.get(2).getEmail()).isEqualTo("bubu@dudu.com");
    }

    @Test
    void should_GetEmptyEmployeeList_When_NoData() {
        List<EmployeeDto> actual = employeeService.getAllEmployees();
        assertThat(actual).isEmpty();
    }

    @Test
    void should_UpdateEmployee_Successfully() {
        EmployeeDto createNewEmployee = getCreateEmployeeDTO();
        employeeService.addEmployee(createNewEmployee);

        EmployeeDto payload = getUpdateEmployeeDTO();
        employeeService.updateEmployee(payload, "john@doe.com");

        EmployeeDto updatedEmployee = employeeService.getEmployee("new_john@doe.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("New John");
        assertThat(updatedEmployee.getLastName()).isEqualTo("New Doe");
        assertThat(updatedEmployee.getEmail()).isEqualTo("new_john@doe.com");
    }

    private EmployeeDto getUpdateEmployeeDTO() {
        return EmployeeDtoBuilder.anEmployeeDto()
                .withFirstName("New John")
                .withLastName("New Doe")
                .withEmail("new_john@doe.com")
                .build();
    }

    @Test
    void should_Failed_UpdateEmployee_When_EmployeeDoesNotExist() {
        EmployeeDto payload = getUpdateEmployeeDTO();
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(payload, "john@doe.com"));
    }

    @Test
    void should_Failed_UpdateEmployee_When_Conflict_With_OtherUserEmail() {
        EmployeeDto employee1 = EmployeeDtoBuilder.anEmployeeDto().withEmail("john@doe.com").build();
        EmployeeDto employee2 = EmployeeDtoBuilder.anEmployeeDto().withEmail("other_user_email@doe.com").build();

        employeeService.addEmployee(employee1);
        employeeService.addEmployee(employee2);

        EmployeeDto payload = getUpdateEmployeeConflictEmailDTO();
        assertThrows(EmployeeAlreadyExistException.class, () -> employeeService.updateEmployee(payload, "john@doe.com"));
    }

    private EmployeeDto getUpdateEmployeeConflictEmailDTO() {
        return EmployeeDtoBuilder.anEmployeeDto()
                .withFirstName("New John")
                .withLastName("New Doe")
                .withEmail("other_user_email@doe.com")
                .build();
    }

    @Test
    void should_DeleteEmployee_Successfully() {
        employeeService.addEmployee(getCreateEmployeeDTO());
        employeeService.deleteEmployee("john@doe.com");

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployee("john@doe.com"));
    }

    @Test
    void should_Failed_DeleteEmployee_When_EmployeeDoesNotExist() {
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee("john@doe.com"));
    }
}