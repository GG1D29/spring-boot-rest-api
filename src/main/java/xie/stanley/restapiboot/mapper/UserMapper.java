package xie.stanley.restapiboot.mapper;

import org.mapstruct.Mapper;
import xie.stanley.restapiboot.dto.UserDto;
import xie.stanley.restapiboot.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toModel(UserDto dto);

    List<UserDto> toDTO(List<User> model);
}
