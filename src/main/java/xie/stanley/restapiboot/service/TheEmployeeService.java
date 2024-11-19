package xie.stanley.restapiboot.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xie.stanley.restapiboot.dto.EmployeeDto;
import xie.stanley.restapiboot.entity.Employee;
import xie.stanley.restapiboot.exception.EmployeeAlreadyExistException;
import xie.stanley.restapiboot.exception.EmployeeNotFoundException;
import xie.stanley.restapiboot.repository.EmployeeRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TheEmployeeService implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public void addEmployee(EmployeeDto dto) {
        boolean isExist = employeeRepository.existsByEmail(dto.getEmail());
        if (isExist) {
            throw new EmployeeAlreadyExistException(dto.getEmail());
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);

        employeeRepository.save(employee);
    }

    @Override
    public EmployeeDto getEmployee(String email) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if (employee.isEmpty()) {
            throw new EmployeeNotFoundException(email);
        }

        EmployeeDto dto = new EmployeeDto();
        BeanUtils.copyProperties(employee.get(), dto);
        return dto;
    }

    @Override
    public void getAllEmployees() {

    }

    @Override
    public void updateEmployee(EmployeeDto dto, int id) {

    }

    @Override
    public void deleteEmployee(int id) {

    }
}
