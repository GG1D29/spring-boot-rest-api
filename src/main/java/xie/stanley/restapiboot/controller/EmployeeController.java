package xie.stanley.restapiboot.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xie.stanley.restapiboot.dto.EmployeeDto;
import xie.stanley.restapiboot.service.EmployeeService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();

        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping("/{email}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable String email) {
        EmployeeDto employee = employeeService.getEmployee(email);

        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @PostMapping
    public ResponseEntity<Void> addEmployee(@RequestBody @Valid EmployeeDto employee) {
        employeeService.addEmployee(employee);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{email}")
    public ResponseEntity<Void> updateEmployee(@RequestBody EmployeeDto employee, @PathVariable String email) {
        employeeService.updateEmployee(employee, email);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String email) {
        employeeService.deleteEmployee(email);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
