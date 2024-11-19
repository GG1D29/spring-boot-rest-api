package xie.stanley.restapiboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import xie.stanley.restapiboot.dto.EmployeeDTO;
import xie.stanley.restapiboot.entity.Employee;
import xie.stanley.restapiboot.exception.EmployeeAlreadyExistException;
import xie.stanley.restapiboot.repository.EmployeeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @InjectMocks
    private TheEmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Captor
    private ArgumentCaptor<Employee> employeeCaptor;

    @Test
    void should_AddEmployee_Successful() {
        Mockito.when(employeeRepository.existsByEmail("john@doe.com")).thenReturn(false);

        employeeService.addEmployee(createEmployeeDTO());

        Mockito.verify(employeeRepository).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();
        assertThat(savedEmployee.getFirstName()).isEqualTo("John");
        assertThat(savedEmployee.getLastName()).isEqualTo("Doe");
        assertThat(savedEmployee.getEmail()).isEqualTo("john@doe.com");
    }

    @Test
    void should_Failed_AddEmployee_When_EmailExist() {
        Mockito.when(employeeRepository.existsByEmail("john@doe.com")).thenReturn(true);
        assertThrows(EmployeeAlreadyExistException.class, () -> employeeService.addEmployee(createEmployeeDTO()));
    }

    private EmployeeDTO createEmployeeDTO() {
        return EmployeeDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();
    }

    @Test
    void getEmployee() {
    }

    @Test
    void getAllEmployees() {
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void deleteEmployee() {
    }
}