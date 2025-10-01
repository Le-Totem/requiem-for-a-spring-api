package fr.afpa.requiem_for_a_spring;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import fr.afpa.requiem_for_a_spring.config.SecurityConfigForTest;
import fr.afpa.requiem_for_a_spring.config.jwt.JwtService;
import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.dtos.UserRoleDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.services.InvitationService;
import fr.afpa.requiem_for_a_spring.services.UserService;
import fr.afpa.requiem_for_a_spring.web.controllers.UserController;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(UserController.class)
@Import(SecurityConfigForTest.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private InvitationService invitationService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    public void getAll() throws Exception {
        UserDto user1 = new UserDto();
        user1.setId(UUID.randomUUID());
        user1.setFirstname("Michel");
        user1.setLastname("Dupont");
        user1.setEmail("michel@test.com");

        UserDto user2 = new UserDto();
        user2.setId(UUID.randomUUID());
        user2.setFirstname("Claire");
        user2.setLastname("Martin");
        user2.setEmail("claire@test.com");

        List<UserDto> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstname").value("Michel"))
                .andExpect(jsonPath("$[1].firstname").value("Claire"));
    }

    @Test
    public void getOne() throws Exception {
        UserDto user = new UserDto();
        user.setId(UUID.randomUUID());
        user.setFirstname("Michel");
        user.setLastname("Dupont");
        user.setEmail("michel@test.com");

        when(userService.getOneUserById(user.getId())).thenReturn(user);

        mockMvc.perform(get("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Michel"))
                .andExpect(jsonPath("$.lastname").value("Dupont"))
                .andExpect(jsonPath("$.email").value("michel@test.com"));
    }

    @Test
    public void getUsersOfGroup() throws Exception {
        UserDto user1 = new UserDto();
        user1.setId(UUID.randomUUID());
        user1.setFirstname("Michel");
        user1.setLastname("Dupont");
        user1.setEmail("michel@test.com");

        UserDto user2 = new UserDto();
        user2.setId(UUID.randomUUID());
        user2.setFirstname("Claire");
        user2.setLastname("Martin");
        user2.setEmail("claire@test.com");

        Group group = new Group();
        group.setId(1);
        group.setName("Chorale");

        List<UserDto> users = Arrays.asList(user1, user2);

        when(userService.getAllUsersByIdGroup(group.getId())).thenReturn(users);

        mockMvc.perform(get("/api/users/group/" + group.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstname").value("Michel"))
                .andExpect(jsonPath("$[1].firstname").value("Claire"));
    }

    @Test
    public void testPatchOneUser() throws Exception {
        UserDto originalUser = new UserDto();
        originalUser.setId(UUID.randomUUID());
        originalUser.setFirstname("Michel");
        originalUser.setLastname("Dupont");
        originalUser.setEmail("michel@test.com");

        UserDto updatedUser = new UserDto();
        updatedUser.setId(originalUser.getId());
        updatedUser.setFirstname("Johnny");
        updatedUser.setLastname("Dupont");
        updatedUser.setEmail("johnny@test.com");

        when(userService.updateUser(eq(originalUser.getId()), any(UserDto.class)))
                .thenReturn(updatedUser);

        String jsonBody = """
                {
                  "firstname": "Johnny",
                  "lastname": "Dupont",
                  "email": "johnny@test.com"
                }
                """;

        mockMvc.perform(patch("/api/users/" + originalUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Johnny"))
                .andExpect(jsonPath("$.email").value("johnny@test.com"));
    }

    // TODO: faire le test unitaire pour l'invitation d'un utilisateur

    @Test
    public void testUpdateUserRole() throws Exception {
        UUID id_user = UUID.randomUUID();
        Integer id_group = 1;

        UserDto userDto = new UserDto();
        userDto.setId(id_user);
        userDto.setEmail("mimi@mail.com");

        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDto.setRole(Role.MODERATEUR);

        when(userService.updateUserRole(eq(id_user), eq(id_group), any(UserRoleDto.class)))
                .thenReturn(userDto);

        mockMvc.perform(patch("/api/users/group/" + id_group + "/user/" + id_user)
                .contentType("application/json")
                .content("{\"role\":\"MODERATEUR\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id_user.toString()))
                .andExpect(jsonPath("$.email").value("mimi@mail.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        UserDto user = new UserDto();
        user.setId(UUID.randomUUID());
        user.setFirstname("Michel");

        Mockito.doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(1);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return null;
        }).when(userService).deleteUser(eq(user.getId()), any(HttpServletResponse.class));

        // On appelle le controller
        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());

    }
}
