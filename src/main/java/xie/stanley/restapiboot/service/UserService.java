package xie.stanley.restapiboot.service;

import xie.stanley.restapiboot.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUser();

    int addUser(UserDto dto);

    UserDto findUser(int id);
}
