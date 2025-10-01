package fr.afpa.requiem_for_a_spring;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.afpa.requiem_for_a_spring.dtos.UserRoleDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.entities.UserGroup;
import fr.afpa.requiem_for_a_spring.entities.UserGroupId;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.mappers.UserMapper;
import fr.afpa.requiem_for_a_spring.repositories.UserGroupRepository;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import fr.afpa.requiem_for_a_spring.services.UserService;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserGroupRepository userGroupRepository;

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
    public void testFindAllUsersByIdGroup() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstname("Michel");

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstname("Michel");

        Group group = new Group();
        group.setId(1);
        group.setName("Chorale");

        UserGroupId userGroupId = new UserGroupId();
        userGroupId.setId_group(group.getId());
        userGroupId.setId_user(user.getId());

        UserGroup userGroup = new UserGroup();
        userGroup.setId(userGroupId);
        userGroup.setUser(user);
        userGroup.setGroup(group);

        List<User> users = Arrays.asList(user);

        Mockito.when(userRepository.findAllByUserGroups_Group_Id(Mockito.any()))
                .thenReturn(users);

        List<UserDto> result = userService.getAllUsersByIdGroup(group.getId());

        assertEquals(1, result.size());
        assertEquals("Michel", result.get(0).getFirstname());
        Mockito.verify(userRepository).findAllByUserGroups_Group_Id(1);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstname("Michel");

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstname("Johnny");

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(userMapper.convertToDto(Mockito.any(User.class)))
                .thenAnswer(invocation -> {
                    User savedUser = invocation.getArgument(0);
                    return new UserDto(savedUser);
                });

        UserDto result = userService.updateUser(user.getId(), userDto);

        assertEquals("Johnny", result.getFirstname());
        Mockito.verify(userRepository).findById(user.getId());
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    public void testUpdateUserRole() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@mail.com");
        user.setPassword("secret");

        Group group = new Group();
        group.setId(1);
        group.setName("Test Group");

        UserGroup userGroup = new UserGroup(user, group, Role.UTILISATEUR);
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDto.setRole(Role.MODERATEUR);

        when(userGroupRepository.findById(new UserGroupId(user.getId(), group.getId())))
                .thenReturn(Optional.of(userGroup));
        when(userGroupRepository.save(any(UserGroup.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = userService.updateUserRole(user.getId(), group.getId(), userRoleDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(Role.MODERATEUR, userGroup.getRole());

        Mockito.verify(userGroupRepository).findById(new UserGroupId(user.getId(), group.getId()));
        Mockito.verify(userGroupRepository).save(userGroup);
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setId(UUID.randomUUID());

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);

        userService.deleteUser(user.getId(), response);

        Mockito.verify(userRepository).deleteById(user.getId());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
