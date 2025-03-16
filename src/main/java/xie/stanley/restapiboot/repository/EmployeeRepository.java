package xie.stanley.restapiboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xie.stanley.restapiboot.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

    void deleteByEmail(String email);
}