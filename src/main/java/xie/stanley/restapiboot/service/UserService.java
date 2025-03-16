package xie.stanley.restapiboot.service;

import xie.stanley.restapiboot.dto.UserDto;
import xie.stanley.restapiboot.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUser();

    void addUser(UserDto dto);

    User findUser(int id);
}
