package xie.stanley.restapiboot.service;

import xie.stanley.restapiboot.dto.EmployeeDTO;

public interface EmployeeService {
    void addEmployee(EmployeeDTO dto);

    void getEmployee(int id);

    void getAllEmployees();

    void updateEmployee(EmployeeDTO dto, int id);

    void deleteEmployee(int id);
}
