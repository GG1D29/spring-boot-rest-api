package xie.stanley.restapiboot.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xie.stanley.restapiboot.dto.EmployeeDTO;
import xie.stanley.restapiboot.entity.Employee;
import xie.stanley.restapiboot.exception.EmployeeAlreadyExistException;
import xie.stanley.restapiboot.repository.EmployeeRepository;

@Service
@AllArgsConstructor
public class TheEmployeeService implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public void addEmployee(EmployeeDTO dto) {
        boolean isExist = employeeRepository.existsByEmail(dto.getEmail());
        if (isExist) {
            throw new EmployeeAlreadyExistException(dto.getEmail());
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);

        employeeRepository.save(employee);
    }

    @Override
    public void getEmployee(int id) {

    }

    @Override
    public void getAllEmployees() {

    }

    @Override
    public void updateEmployee(EmployeeDTO dto, int id) {

    }

    @Override
    public void deleteEmployee(int id) {

    }
}
