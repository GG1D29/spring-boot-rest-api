package xie.stanley.restapiboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import xie.stanley.restapiboot.dto.EmployeeDto;
import xie.stanley.restapiboot.dto.EmployeeDtoBuilder;
import xie.stanley.restapiboot.entity.Employee;
import xie.stanley.restapiboot.exception.EmployeeAlreadyExistException;
import xie.stanley.restapiboot.exception.EmployeeNotFoundException;
import xie.stanley.restapiboot.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @InjectMocks
    private TheEmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Captor
    private ArgumentCaptor<Employee> employeeCaptor;

    @Test
    void should_AddEmployee_Successfully() {
        Mockito.when(employeeRepository.existsByEmail("john@doe.com")).thenReturn(false);

        employeeService.addEmployee(getCreateEmployeeDTO());

        Mockito.verify(employeeRepository).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();
        assertThat(savedEmployee.getFirstName()).isEqualTo("John");
        assertThat(savedEmployee.getLastName()).isEqualTo("Doe");
        assertThat(savedEmployee.getEmail()).isEqualTo("john@doe.com");
    }

    @Test
    void should_Failed_AddEmployee_When_EmailAlreadyExist() {
        Mockito.when(employeeRepository.existsByEmail("john@doe.com")).thenReturn(true);
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
    void should_ReturnEmployeeByEmail_Successfully() {
        Employee employee = new Employee();
        employee.setEmail("john@doe.com");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        Mockito.when(employeeRepository.findByEmail("john@doe.com")).thenReturn(Optional.of(employee));

        EmployeeDto actual = employeeService.getEmployee("john@doe.com");
        assertThat(actual.getFirstName()).isEqualTo("John");
        assertThat(actual.getLastName()).isEqualTo("Doe");
        assertThat(actual.getEmail()).isEqualTo("john@doe.com");
    }

    @Test
    void should_Failed_ReturnEmployeeByEmail_When_EmailDoesNotExist() {
        Mockito.when(employeeRepository.findByEmail("john@doe.com")).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployee("john@doe.com"));
    }

    @Test
    void should_GetAllEmployees_Successfully() {
        Mockito.when(employeeRepository.findAll()).thenReturn(getAllEmployees());

        List<EmployeeDto> actual = employeeService.getAllEmployees();
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).getFirstName()).isEqualTo("John");
        assertThat(actual.get(0).getLastName()).isEqualTo("Doe");
        assertThat(actual.get(0).getEmail()).isEqualTo("john@doe.com");

        assertThat(actual.get(1).getFirstName()).isEqualTo("Budi");
        assertThat(actual.get(1).getLastName()).isEqualTo("Andi");
        assertThat(actual.get(1).getEmail()).isEqualTo("budi@doe.com");
    }

    @Test
    void should_GetEmptyEmployeeList_When_NoData() {
        Mockito.when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        List<EmployeeDto> actual = employeeService.getAllEmployees();
        assertThat(actual).isEmpty();
    }

    private List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        Employee e1 = new Employee();
        e1.setFirstName("John");
        e1.setLastName("Doe");
        e1.setEmail("john@doe.com");

        Employee e2 = new Employee();
        e2.setFirstName("Budi");
        e2.setLastName("Andi");
        e2.setEmail("budi@doe.com");

        employees.add(e1);
        employees.add(e2);

        return employees;
    }

    @Test
    void should_UpdateEmployee_Successfully() {
        Employee employee = new Employee();
        employee.setId(5L);
        employee.setEmail("john@doe.com");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        Mockito.when(employeeRepository.findByEmail("john@doe.com")).thenReturn(Optional.of(employee));

        EmployeeDto payload = getUpdateEmployeeDTO();
        employeeService.updateEmployee(payload, "john@doe.com");

        Mockito.verify(employeeRepository).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();
        assertThat(savedEmployee.getId()).isEqualTo(5L);
        assertThat(savedEmployee.getFirstName()).isEqualTo("New John");
        assertThat(savedEmployee.getLastName()).isEqualTo("New Doe");
        assertThat(savedEmployee.getEmail()).isEqualTo("new_john@doe.com");
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
        Mockito.when(employeeRepository.findByEmail("john@doe.com")).thenReturn(Optional.empty());

        EmployeeDto payload = getUpdateEmployeeDTO();
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(payload, "john@doe.com"));
    }

    @Test
    void should_Failed_UpdateEmployee_When_Conflict_With_OtherUserEmail() {
        Employee employee = new Employee();
        employee.setId(5L);
        employee.setEmail("john@doe.com");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        Mockito.when(employeeRepository.findByEmail("john@doe.com")).thenReturn(Optional.of(employee));

        Mockito.when(employeeRepository.findByEmail("other_user_email@doe.com")).thenReturn(Optional.of(new Employee()));

        EmployeeDto payload = getUpdateEmployeeConflictEmailDTO();
        assertThrows(EmployeeAlreadyExistException.class, () -> employeeService.updateEmployee(payload, "john@doe.com"));

        Mockito.verify(employeeRepository, never()).save(any(Employee.class));
    }

    private EmployeeDto getUpdateEmployeeConflictEmailDTO() {
        return EmployeeDtoBuilder.anEmployeeDto()
                .withFirstName("New John")
                .withLastName("New Doe")
                .withEmail("other_user_email@doe.com")
                .build();
    }

    @Test
    void deleteEmployee() {
    }
}