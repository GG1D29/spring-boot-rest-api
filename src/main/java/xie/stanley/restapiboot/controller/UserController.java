package xie.stanley.restapiboot.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xie.stanley.restapiboot.dto.UserDto;
import xie.stanley.restapiboot.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUser() {
        List<UserDto> users = userService.findAllUser();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto dto) {
        int userId = userService.addUser(dto);

        return ResponseEntity.ok(userId);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable int userId) {
        UserDto user = userService.findUser(userId);

        return ResponseEntity.ok(user);
    }
}
