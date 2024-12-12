package xie.stanley.restapiboot.service;

import xie.stanley.restapiboot.dto.EmployeeDto;
import xie.stanley.restapiboot.exception.EmployeeAlreadyExistException;
import xie.stanley.restapiboot.exception.EmployeeNotFoundException;

import java.util.List;

public interface EmployeeService {
    void addEmployee(EmployeeDto dto) throws EmployeeAlreadyExistException;

    EmployeeDto getEmployee(String email) throws EmployeeNotFoundException;

    List<EmployeeDto> getAllEmployees();

    void updateEmployee(EmployeeDto dto, String currentEmail);

    void deleteEmployee(String email);
}
