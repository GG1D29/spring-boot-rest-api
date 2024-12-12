package xie.stanley.restapiboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xie.stanley.restapiboot.dto.EmployeeDto;
import xie.stanley.restapiboot.dto.EmployeeDtoBuilder;
import xie.stanley.restapiboot.exception.EmployeeAlreadyExistException;
import xie.stanley.restapiboot.exception.EmployeeNotFoundException;
import xie.stanley.restapiboot.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_GetAllEmployees_Successfully() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(getEmployees());

        mockMvc.perform(get("/employees"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].email", is("john@doe.com")))
                .andExpect(jsonPath("$[1].firstName", is("Mary")))
                .andExpect(jsonPath("$[1].lastName", is("Jane")))
                .andExpect(jsonPath("$[1].email", is("mary@jane.com")));
    }

    @Test
    void should_GetAllEmployees_Successfully_When_EmptyList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/employees"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    void should_GetEmployee_Successfully_ByEmailId() throws Exception {
        when(employeeService.getEmployee("some-email")).thenReturn(getAnEmployee());

        mockMvc.perform(get("/employees/some-email"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john@doe.com")));
    }

    private EmployeeDto getAnEmployee() {
        return EmployeeDtoBuilder.anEmployeeDto()
                .withFirstName("John")
                .withLastName("Doe")
                .withEmail("john@doe.com")
                .build();
    }

    private List<EmployeeDto> getEmployees() {
        List<EmployeeDto> employees = new ArrayList<>();
        EmployeeDto employee1 = getAnEmployee();
        employees.add(employee1);

        EmployeeDto employee2 = EmployeeDtoBuilder.anEmployeeDto()
                .withFirstName("Mary")
                .withLastName("Jane")
                .withEmail("mary@jane.com")
                .build();

        employees.add(employee2);

        return employees;
    }

    @Test
    void should_Failed_GetEmployee_When_EmailIdNotFound() throws Exception {
        when(employeeService.getEmployee("some-email")).thenThrow(new EmployeeNotFoundException("some-email"));

        mockMvc.perform(get("/employees/some-email"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Employee not found: some-email")));
    }

    @Test
    void should_AddEmployee_Successfully() throws Exception {
        EmployeeDto employee = getAnEmployee();
        String payload = objectMapper.writeValueAsString(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated());
    }

    @Test
    void should_Failed_AddEmployee_When_EmailIdAlreadyExists() throws Exception {
        doThrow(new EmployeeAlreadyExistException("some-email"))
                .when(employeeService).addEmployee(any(EmployeeDto.class));

        EmployeeDto employee = getAnEmployee();
        String payload = objectMapper.writeValueAsString(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Employee already exists with this email: some-email")));
    }

    @Test
    void should_UpdateEmployee_Successfully() throws Exception {
        EmployeeDto employee = getAnEmployee();
        String payload = objectMapper.writeValueAsString(employee);

        mockMvc.perform(put("/employees/some-email")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_Failed_UpdateEmployee_When_EmailIdNotFound() throws Exception {
        doThrow(new EmployeeNotFoundException("some-email"))
                .when(employeeService).updateEmployee(any(EmployeeDto.class), anyString());

        EmployeeDto employee = getAnEmployee();
        String payload = objectMapper.writeValueAsString(employee);

        mockMvc.perform(put("/employees/some-email")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Employee not found: some-email")));
    }

    @Test
    void should_Failed_UpdateEmployee_When_OtherEmailIdAlreadyExists() throws Exception {
        doThrow(new EmployeeAlreadyExistException("some-email"))
                .when(employeeService).updateEmployee(any(EmployeeDto.class), anyString());

        EmployeeDto employee = getAnEmployee();
        String payload = objectMapper.writeValueAsString(employee);

        mockMvc.perform(put("/employees/some-email")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Employee already exists with this email: some-email")));
    }

    @Test
    void should_DeleteEmployee_Successfully() throws Exception {

        mockMvc.perform(delete("/employees/some-email"))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_Failed_DeleteEmployee_When_EmailIdNotFound() throws Exception {
        doThrow(new EmployeeNotFoundException("some-email")).when(employeeService).deleteEmployee("some-email");

        mockMvc.perform(delete("/employees/some-email"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Employee not found: some-email")));
    }
}