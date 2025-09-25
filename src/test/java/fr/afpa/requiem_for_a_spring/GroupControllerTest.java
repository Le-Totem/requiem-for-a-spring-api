package fr.afpa.requiem_for_a_spring;

import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import fr.afpa.requiem_for_a_spring.mappers.GroupMapper;
import fr.afpa.requiem_for_a_spring.services.GroupService;
import fr.afpa.requiem_for_a_spring.web.controllers.GroupController;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GroupController.class)
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private GroupService groupService;
    @Autowired
    private GroupMapper groupMapper;

    @Test
    void testGetById() throws Exception {
        GroupDto dto = new GroupDto();
        dto.setId(1);
        dto.setName("Jazz Lovers");

        Mockito.when(groupService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/groups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jazz Lovers"));
    }
}
