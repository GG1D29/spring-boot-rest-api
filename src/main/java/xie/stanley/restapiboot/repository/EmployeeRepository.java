package xie.stanley.restapiboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xie.stanley.restapiboot.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);
}