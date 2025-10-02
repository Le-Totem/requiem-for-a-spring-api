package fr.afpa.requiem_for_a_spring;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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
import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import fr.afpa.requiem_for_a_spring.dtos.InvitationDto;
import fr.afpa.requiem_for_a_spring.entities.MusicPiece;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import fr.afpa.requiem_for_a_spring.services.GroupService;
import fr.afpa.requiem_for_a_spring.services.InvitationService;
import fr.afpa.requiem_for_a_spring.web.controllers.GroupController;

@WebMvcTest(GroupController.class)
@Import(SecurityConfigForTest.class)
@AutoConfigureMockMvc(addFilters = false)
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupService groupService;

    @MockitoBean
    private MusicPieceRepository musicPieceRepository;

    @MockitoBean
    private InvitationService invitationService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    public void getAllGroups() throws Exception {
        GroupDto group1 = new GroupDto();
        group1.setId(1);
        group1.setName("Chorale");

        GroupDto group2 = new GroupDto();
        group2.setId(2);
        group2.setName("Orchestre");

        List<GroupDto> groups = Arrays.asList(group1, group2);
        when(groupService.findAll()).thenReturn(groups);

        mockMvc.perform(get("/api/groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Chorale"))
                .andExpect(jsonPath("$[1].name").value("Orchestre"));
    }

    @Test
    public void getGroupById() throws Exception {
        GroupDto group = new GroupDto();
        group.setId(1);
        group.setName("Chorale");

        when(groupService.findById(group.getId())).thenReturn(group);

        mockMvc.perform(get("/api/groups/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Chorale"));
    }

    @Test
    public void getMusicByGroup() throws Exception {
        MusicPiece piece1 = new MusicPiece();
        piece1.setId(1);
        piece1.setTitle("Requiem");

        MusicPiece piece2 = new MusicPiece();
        piece2.setId(2);
        piece2.setTitle("Ave Maria");

        when(musicPieceRepository.findAllByGroup_Id(1)).thenReturn(Arrays.asList(piece1, piece2));

        mockMvc.perform(get("/api/groups/1/track")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Requiem"))
                .andExpect(jsonPath("$[1].title").value("Ave Maria"));
    }

    @Test
    public void testGetInvitations() throws Exception {
        Integer id_group = 1;

        InvitationDto invitation1 = new InvitationDto();
        invitation1.setId(1);
        invitation1.setEmail("jean@mail.com");

        InvitationDto invitation2 = new InvitationDto();
        invitation2.setId(2);
        invitation2.setEmail("nina@mail.com");

        List<InvitationDto> invitations = Arrays.asList(invitation1, invitation2);

        when(invitationService.getAllInvitations(id_group)).thenReturn(invitations);

        mockMvc.perform(get("/api/groups/" + id_group + "/invitations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").value("jean@mail.com"))
                .andExpect(jsonPath("$[1].email").value("nina@mail.com"));
    }

    @Test
    public void createGroup() throws Exception {
        GroupDto groupDto = new GroupDto();
        groupDto.setName("Nouvelle Chorale");

        GroupDto savedGroup = new GroupDto();
        savedGroup.setId(3);
        savedGroup.setName("Nouvelle Chorale");

        when(groupService.save(any(GroupDto.class))).thenReturn(savedGroup);

        String jsonBody = """
                {
                    "name": "Nouvelle Chorale"
                }
                """;

        mockMvc.perform(post("/api/groups/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Nouvelle Chorale"));
    }

    @Test
    public void deleteGroup() throws Exception {
        Mockito.doNothing().when(groupService).deleteById(1);

        mockMvc.perform(delete("/api/groups/1"))
                .andExpect(status().isNoContent());
    }
}
