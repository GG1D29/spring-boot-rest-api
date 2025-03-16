package xie.stanley.restapiboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xie.stanley.restapiboot.dto.UserDto;
import xie.stanley.restapiboot.exception.UserNotFoundException;
import xie.stanley.restapiboot.mapper.UserMapper;
import xie.stanley.restapiboot.model.User;
import xie.stanley.restapiboot.model.UserType;
import xie.stanley.restapiboot.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private TheUserService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void should_FindAllUser_Successfully() {
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> UserDto = new ArrayList<>();
        when(userMapper.toDTO(users)).thenReturn(UserDto);

        List<UserDto> actual = service.findAllUser();
        assertThat(actual).isEqualTo(UserDto);
    }

    @Test
    void should_AddUser_Successfully() {

        service.addUser(getAddUserDto());

        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getAddress()).isEqualTo("jalan jalan");
        assertThat(capturedUser.getName()).isEqualTo("naamaku");
    }

    private UserDto getAddUserDto() {
        UserDto dto = new UserDto();
        dto.setAddress("jalan jalan");
        dto.setName("naamaku");

        return dto;
    }

    @Test
    void should_FindUser_Successfully() {
        User user = new User();
        user.setUserType(UserType.LENDER);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDto expected = getUserDto();
        when(userMapper.toDTO(user)).thenReturn(expected);

        UserDto actual = service.findUser(1);
        assertThat(actual).isEqualTo(expected);
    }

    private UserDto getUserDto() {
        UserDto user = new UserDto();
        user.setUserType(UserType.LENDER);
        user.setName("shenli");
        user.setBirthDate(LocalDate.of(1990, 1, 2));
        user.setAddress("jalan jalan");

        return user;
    }

    @Test
    void should_FindUser_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception e = assertThrows(UserNotFoundException.class, () -> service.findUser(1));
        assertThat(e.getMessage()).isEqualTo("user with id: 1 is not found");

    }
}