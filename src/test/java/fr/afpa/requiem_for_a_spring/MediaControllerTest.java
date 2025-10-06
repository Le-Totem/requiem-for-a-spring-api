package fr.afpa.requiem_for_a_spring;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import fr.afpa.requiem_for_a_spring.config.SecurityConfigForTest;
import fr.afpa.requiem_for_a_spring.dtos.MediaDto;
import fr.afpa.requiem_for_a_spring.services.MediaService;

@WebMvcTest(fr.afpa.requiem_for_a_spring.web.controllers.MediaController.class)
@Import(SecurityConfigForTest.class)
public class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MediaService mediaService;

    @Test
    public void getAllMedia() throws Exception {
        MediaDto media1 = new MediaDto();
        media1.setId(1);
        media1.setTitle("Audio 1");

        MediaDto media2 = new MediaDto();
        media2.setId(2);
        media2.setTitle("Video 1");

        List<MediaDto> mediaList = Arrays.asList(media1, media2);
        when(mediaService.getAll()).thenReturn(mediaList);

        mockMvc.perform(get("/api/media")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Audio 1"))
                .andExpect(jsonPath("$[1].title").value("Video 1"));
    }

    @Test
    public void getMediaById() throws Exception {
        MediaDto media = new MediaDto();
        media.setId(1);
        media.setTitle("Audio 1");

        when(mediaService.getById(1)).thenReturn(media);

        mockMvc.perform(get("/api/media/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Audio 1"));
    }

    @Test
    public void createMedia() throws Exception {
        MediaDto dto = new MediaDto();
        dto.setTitle("Audio 2");

        MediaDto created = new MediaDto();
        created.setId(3);
        created.setTitle("Audio 2");

        when(mediaService.createMedia(any(MediaDto.class))).thenReturn(created);

        String jsonBody = """
                {
                    "title": "Audio 2"
                }
                """;

        mockMvc.perform(post("/api/media")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Audio 2"));
    }

    @Test
    public void deleteMedia() throws Exception {
        doNothing().when(mediaService).deleteById(1);

        mockMvc.perform(delete("/api/media/1"))
                .andExpect(status().isNoContent());
    }
}
