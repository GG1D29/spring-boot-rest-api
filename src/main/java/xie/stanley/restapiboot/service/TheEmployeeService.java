package xie.stanley.restapiboot.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xie.stanley.restapiboot.dto.EmployeeDto;
import xie.stanley.restapiboot.model.Employee;
import xie.stanley.restapiboot.exception.EmployeeAlreadyExistException;
import xie.stanley.restapiboot.exception.EmployeeNotFoundException;
import xie.stanley.restapiboot.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TheEmployeeService implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public void addEmployee(EmployeeDto dto) throws EmployeeAlreadyExistException {
        boolean isExist = employeeRepository.existsByEmail(dto.getEmail());
        if (isExist) {
            throw new EmployeeAlreadyExistException(dto.getEmail());
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);

        employeeRepository.save(employee);
    }

    @Override
    public EmployeeDto getEmployee(String email) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if (employee.isEmpty()) {
            throw new EmployeeNotFoundException(email);
        }

        EmployeeDto dto = new EmployeeDto();
        BeanUtils.copyProperties(employee.get(), dto);
        return dto;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDto> employeeDto = new ArrayList<>();

        for (Employee employee : employees) {
            EmployeeDto dto = new EmployeeDto();
            BeanUtils.copyProperties(employee, dto);
            employeeDto.add(dto);
        }

        return employeeDto;
    }

    @Override
    public void updateEmployee(EmployeeDto dto, String currentEmail) throws EmployeeNotFoundException,
            EmployeeAlreadyExistException {

        Employee employee = getCurrentEmployee(currentEmail);
        validateNewEmail(employee, dto);

        BeanUtils.copyProperties(dto, employee);
        employeeRepository.save(employee);
    }

    private Employee getCurrentEmployee(String currentEmail) {
        Optional<Employee> currentEmployee = employeeRepository.findByEmail(currentEmail);
        return currentEmployee.orElseThrow(() -> new EmployeeNotFoundException(currentEmail));
    }

    private void validateNewEmail(Employee employee, EmployeeDto dto) {
        if (employee.getEmail().equals(dto.getEmail())) {
            return;
        }

        Optional<Employee> otherEmployee = employeeRepository.findByEmail(dto.getEmail());
        if (otherEmployee.isPresent()) {
            throw new EmployeeAlreadyExistException(dto.getEmail());
        }
    }

    @Override
    @Transactional
    public void deleteEmployee(String email) throws EmployeeNotFoundException {
        boolean isExist = employeeRepository.existsByEmail(email);
        if (!isExist) {
            throw new EmployeeNotFoundException(email);
        }

        employeeRepository.deleteByEmail(email);
    }
}
