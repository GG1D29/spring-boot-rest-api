package xie.stanley.restapiboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xie.stanley.restapiboot.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(int id);
}
