package fr.afpa.requiem_for_a_spring;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import fr.afpa.requiem_for_a_spring.config.SecurityConfigForTest;
import fr.afpa.requiem_for_a_spring.config.jwt.JwtService;
import fr.afpa.requiem_for_a_spring.dtos.GenreDto;
import fr.afpa.requiem_for_a_spring.dtos.MediaDto;
import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.entities.MusicPiece;
import fr.afpa.requiem_for_a_spring.services.GenreService;
import fr.afpa.requiem_for_a_spring.services.MediaService;
import fr.afpa.requiem_for_a_spring.services.MusicPieceService;
import fr.afpa.requiem_for_a_spring.web.controllers.MusicPieceController;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(MusicPieceController.class)
@Import(SecurityConfigForTest.class)
@AutoConfigureMockMvc(addFilters = false)
public class MusicPieceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MusicPieceService musicPieceService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private MediaService mediaService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    public void testGetAll() throws Exception {
        MusicPieceDto musicPieceDto1 = new MusicPieceDto();
        musicPieceDto1.setId(1);
        musicPieceDto1.setTitle("Bella Ciao");

        MusicPieceDto musicPieceDto2 = new MusicPieceDto();
        musicPieceDto2.setId(2);
        musicPieceDto2.setTitle("Skibidi");

        List<MusicPieceDto> musicPieces = Arrays.asList(musicPieceDto1, musicPieceDto2);

        when(musicPieceService.getAllMusicPieces()).thenReturn(musicPieces);

        mockMvc.perform(get("/api/tracks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Bella Ciao"))
                .andExpect(jsonPath("$[1].title").value("Skibidi"));
    }

    @Test
    public void testGetOne() throws Exception {
        MusicPieceDto musicPieceDto = new MusicPieceDto();
        musicPieceDto.setId(2);
        musicPieceDto.setTitle("Skibidi");

        when(musicPieceService.getOneMusicPiece(musicPieceDto.getId())).thenReturn(musicPieceDto);

        mockMvc.perform(get("/api/tracks/" + musicPieceDto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Skibidi"));
    }

    @Test
    public void testGetAllByIdGroup() throws Exception {
        MusicPieceDto musicPieceDto1 = new MusicPieceDto();
        musicPieceDto1.setId(1);
        musicPieceDto1.setTitle("Bella Ciao");

        MusicPieceDto musicPieceDto2 = new MusicPieceDto();
        musicPieceDto2.setId(2);
        musicPieceDto2.setTitle("Skibidi");

        List<MusicPieceDto> musicPieces = Arrays.asList(musicPieceDto1, musicPieceDto2);

        Group group = new Group();
        group.setId(1);

        when(musicPieceService.getAllByIdGroup(group.getId())).thenReturn(musicPieces);

        mockMvc.perform(get("/api/tracks/group/" + group.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Bella Ciao"))
                .andExpect(jsonPath("$[1].title").value("Skibidi"));
    }

    @Test
    public void testGetAllGenres() throws Exception {
        GenreDto genreDto1 = new GenreDto();
        genreDto1.setId(1);
        genreDto1.setName("Rap");

        GenreDto genreDto2 = new GenreDto();
        genreDto2.setId(2);
        genreDto2.setName("Pop");

        MusicPiece musicPiece = new MusicPiece();
        musicPiece.setId(1);

        List<GenreDto> genres = Arrays.asList(genreDto1, genreDto2);

        when(genreService.getAllGenresByIdMusicPiece(1)).thenReturn(genres);

        mockMvc.perform(get("/api/tracks/" + musicPiece.getId() + "/all-genres")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Rap"))
                .andExpect(jsonPath("$[1].name").value("Pop"));
    }

    @Test
    public void testGetMediasByIdMusicPiece() throws Exception {
        MediaDto media = new MediaDto();
        media.setId(1);
        media.setTitle("Partition piano");

        MusicPiece musicPiece = new MusicPiece();
        musicPiece.setId(1);

        List<MediaDto> medias = Arrays.asList(media);

        when(mediaService.getMediasByIdMusicPiece(musicPiece.getId())).thenReturn(medias);

        mockMvc.perform(get("/api/tracks/" + musicPiece.getId() + "/medias")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Partition piano"));
    }

    @Test
    public void testCreateMusicPiece() throws Exception {
        Integer id_group = 5;

        MusicPieceDto musicPieceDto = new MusicPieceDto();
        musicPieceDto.setId(1);
        musicPieceDto.setTitle("Bella Ciao");
        musicPieceDto.setAuthor("Money Heist");
        musicPieceDto.setDescription("Musique de La Casa De Papel");

        when(musicPieceService.createMusicPiece(eq(id_group), Mockito.any(MusicPieceDto.class)))
                .thenReturn(musicPieceDto);

        mockMvc.perform(post("/api/tracks/" + id_group + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "title": "Bella Ciao",
                                "author": "Money Heist",
                                "description": "Musique de La Casa De Papel"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Bella Ciao"))
                .andExpect(jsonPath("$.author").value("Money Heist"))
                .andExpect(jsonPath("$.description").value("Musique de La Casa De Papel"));
    }

    @Test
    public void testCreateGenre() throws Exception {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(1);
        genreDto.setName("Rap");

        when(genreService.createGenre(Mockito.any(GenreDto.class)))
                .thenReturn(genreDto);

        mockMvc.perform(post("/api/tracks/add-genre")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name": "Rap"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rap"));
    }

    @Test
    public void testAddGenresToMusicPiece() throws Exception {
        GenreDto genreDto1 = new GenreDto();
        genreDto1.setId(1);
        genreDto1.setName("Rap");

        GenreDto genreDto2 = new GenreDto();
        genreDto2.setId(2);
        genreDto2.setName("Pop");

        List<GenreDto> genres = Arrays.asList(genreDto1, genreDto2);

        when(genreService.addGenres(eq(1), Mockito.anyList()))
                .thenReturn(genres);

        mockMvc.perform(post("/api/tracks/1/add-genre")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            [
                                { "id": 1, "name": "Rap" },
                                { "id": 2, "name": "Pop" }
                            ]
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Rap"))
                .andExpect(jsonPath("$[1].name").value("Pop"));
    }

    @Test
    public void testUpdateMusicPiece() throws Exception {
        MusicPieceDto originalMusicPieceDto = new MusicPieceDto();
        originalMusicPieceDto.setId(1);
        originalMusicPieceDto.setTitle("Bella Ciao");
        originalMusicPieceDto.setAuthor("La Casa De Papel");

        MusicPieceDto savedMusicPieceDto = new MusicPieceDto();
        savedMusicPieceDto.setId(1);
        savedMusicPieceDto.setTitle("Bella Ciao");
        savedMusicPieceDto.setAuthor("Money Heist");

        when(musicPieceService.updateMusicPiece(eq(originalMusicPieceDto.getId()), Mockito.any(MusicPieceDto.class)))
                .thenReturn(savedMusicPieceDto);

        mockMvc.perform(patch("/api/tracks/" + originalMusicPieceDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "title": "Bella Ciao",
                                "author": "Money Heist"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Bella Ciao"))
                .andExpect(jsonPath("$.author").value("Money Heist"));
    }

    @Test
    public void testDeleteMusicPiece() throws Exception {
        MusicPieceDto musicPieceDto = new MusicPieceDto();
        musicPieceDto.setId(1);
        musicPieceDto.setTitle("Bella Ciao");
        musicPieceDto.setAuthor("Money Heist");
        musicPieceDto.setDescription("Musique de La Casa De Papel");

        Mockito.doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(1);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return null;
        }).when(musicPieceService).removeMusicPiece(eq(musicPieceDto.getId()), any(HttpServletResponse.class));

        mockMvc.perform(delete("/api/tracks/" + musicPieceDto.getId()))
                .andExpect(status().isNoContent());
    }
}
