package xie.stanley.restapiboot.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xie.stanley.restapiboot.dto.UserDto;
import xie.stanley.restapiboot.exception.UserNotFoundException;
import xie.stanley.restapiboot.mapper.UserMapper;
import xie.stanley.restapiboot.model.User;
import xie.stanley.restapiboot.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TheUserService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAllUser() {
        List<User> users = userRepository.findAll();
        return userMapper.toDTO(users);
    }

    @Override
    public void addUser(UserDto dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);

        userRepository.save(user);
    }

    @Override
    public User findUser(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
