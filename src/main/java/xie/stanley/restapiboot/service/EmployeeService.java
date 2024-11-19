package xie.stanley.restapiboot.service;

import xie.stanley.restapiboot.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    void addEmployee(EmployeeDto dto);

    EmployeeDto getEmployee(String email);

    List<EmployeeDto> getAllEmployees();

    void updateEmployee(EmployeeDto dto, String currentEmail);

    void deleteEmployee(int id);
}
