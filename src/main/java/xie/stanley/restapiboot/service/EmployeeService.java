package xie.stanley.restapiboot.service;

import xie.stanley.restapiboot.dto.EmployeeDto;

public interface EmployeeService {
    void addEmployee(EmployeeDto dto);

    EmployeeDto getEmployee(String email);

    void getAllEmployees();

    void updateEmployee(EmployeeDto dto, int id);

    void deleteEmployee(int id);
}
