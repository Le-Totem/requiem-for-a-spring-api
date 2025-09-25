package fr.afpa.requiem_for_a_spring;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.mappers.UserMapper;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import fr.afpa.requiem_for_a_spring.services.UserService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindAll() {
        List<User> userList = Arrays.asList(new User());
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        List<UserDto> users = userService.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstname("Michel");

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstname("Michel");

        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user));

        Mockito.when(userMapper.convertToDto(user))
                .thenReturn(userDto);

        UserDto result = userService.getOneUserById(user.getId());

        assertEquals("Michel", result.getFirstname());
        Mockito.verify(userRepository).findById(user.getId());
        Mockito.verify(userMapper).convertToDto(user);
    }

    @Test
    public void testSave() {
        UserDto userDto = new UserDto();
        userDto.setFirstname("Michel");

        User user = new User();
        user.setFirstname("Michel");

    }

}
