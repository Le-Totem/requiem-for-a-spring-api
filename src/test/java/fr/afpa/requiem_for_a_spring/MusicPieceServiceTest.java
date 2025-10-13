package fr.afpa.requiem_for_a_spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.entities.MusicPiece;
import fr.afpa.requiem_for_a_spring.mappers.MusicPieceMapper;
import fr.afpa.requiem_for_a_spring.repositories.GroupRepository;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import fr.afpa.requiem_for_a_spring.services.MusicPieceService;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class MusicPieceServiceTest {
    @Mock
    private MusicPieceRepository musicPieceRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private MusicPieceMapper musicPieceMapper;

    @InjectMocks
    private MusicPieceService musicPieceService;

    @Test
    public void testGetAllMusicPieces() {
        List<MusicPiece> musicPiecesList = Arrays.asList(new MusicPiece());
        Mockito.when(musicPieceRepository.findAll()).thenReturn(musicPiecesList);

        List<MusicPieceDto> musicPieces = musicPieceService.getAllMusicPieces();
        assertEquals(1, musicPieces.size());
    }

    @Test
    public void testGetAllByIdGroup() {
        MusicPiece musicPiece = new MusicPiece();
        musicPiece.setId(1);
        musicPiece.setTitle("Bella Ciao");

        MusicPieceDto musicPieceDto = new MusicPieceDto();
        musicPieceDto.setId(musicPiece.getId());
        musicPieceDto.setTitle("Bella Ciao");

        Group group = new Group();
        group.setId(1);
        group.setName("Chorale");

        List<MusicPiece> musicPieces = Arrays.asList(musicPiece);

        Mockito.when(groupRepository.existsById(group.getId())).thenReturn(true);
        Mockito.when(musicPieceRepository.findAllByGroup_Id(musicPiece.getId())).thenReturn(musicPieces);

        List<MusicPieceDto> result = musicPieceService.getAllByIdGroup(group.getId());

        assertEquals(1, result.size());
        assertEquals("Bella Ciao", result.get(0).getTitle());

        Mockito.verify(groupRepository).existsById(group.getId());
        Mockito.verify(musicPieceRepository).findAllByGroup_Id(group.getId());
    }

    @Test
    public void testGetOneMusicPiece() {
        MusicPiece musicPiece = new MusicPiece();
        musicPiece.setId(1);
        musicPiece.setTitle("Bella Ciao");

        MusicPieceDto musicPieceDto = new MusicPieceDto();
        musicPieceDto.setId(musicPiece.getId());
        musicPieceDto.setTitle("Bella Ciao");

        Mockito.when(musicPieceRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(musicPiece));

        Mockito.when(musicPieceMapper.convertToDto(musicPiece))
                .thenReturn(musicPieceDto);

        MusicPieceDto result = musicPieceService.getOneMusicPiece(musicPiece.getId());

        assertEquals("Bella Ciao", result.getTitle());
        Mockito.verify(musicPieceRepository).findById(musicPiece.getId());
        Mockito.verify(musicPieceMapper).convertToDto(musicPiece);
    }

    @Test
    public void testCreateMusicPiece() {
        Group group = new Group();
        group.setId(5);

        MusicPiece originalMusicPiece = new MusicPiece();
        originalMusicPiece.setId(1);
        originalMusicPiece.setTitle("Bella Ciao");

        MusicPieceDto musicPieceDto = new MusicPieceDto();
        musicPieceDto.setId(originalMusicPiece.getId());
        musicPieceDto.setTitle("Bella Ciao");

        MusicPiece savedMusicPiece = new MusicPiece();
        savedMusicPiece.setId(1);
        savedMusicPiece.setTitle("Bella Ciao");
        savedMusicPiece.setGroup(group);

        Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        Mockito.when(musicPieceMapper.convertToEntity(musicPieceDto)).thenReturn(originalMusicPiece);
        Mockito.when(musicPieceRepository.save(originalMusicPiece)).thenReturn(savedMusicPiece);

        MusicPieceDto result = musicPieceService.createMusicPiece(group.getId(), musicPieceDto);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        assertEquals("Bella Ciao", result.getTitle());

        Mockito.verify(groupRepository).findById(group.getId());
        Mockito.verify(musicPieceMapper).convertToEntity(musicPieceDto);
        Mockito.verify(musicPieceRepository).save(originalMusicPiece);
    }

    @Test
    public void testUpdateMusicPiece() {
        MusicPiece musicPiece = new MusicPiece();
        musicPiece.setId(1);
        musicPiece.setTitle("Bella Ciao");

        MusicPieceDto musicPieceDto = new MusicPieceDto();
        musicPieceDto.setId(musicPiece.getId());
        musicPieceDto.setTitle("Bella Ciao");

        Mockito.when(musicPieceRepository.findById(musicPiece.getId()))
                .thenReturn(Optional.of(musicPiece));
        Mockito.when(musicPieceRepository.save(Mockito.any(MusicPiece.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(musicPieceMapper.convertToDto(Mockito.any(MusicPiece.class)))
                .thenAnswer(invocation -> {
                    MusicPiece savedMusicPiece = invocation.getArgument(0);
                    return new MusicPieceDto(savedMusicPiece);
                });

        MusicPieceDto result = musicPieceService.updateMusicPiece(musicPiece.getId(), musicPieceDto);

        assertEquals("Bella Ciao", result.getTitle());
        Mockito.verify(musicPieceRepository).findById(musicPiece.getId());
        Mockito.verify(musicPieceRepository).save(Mockito.any(MusicPiece.class));
    }

    @Test
    public void testDeleteMusicPiece() {
        MusicPiece musicPiece = new MusicPiece();
        musicPiece.setId(1);

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(musicPieceRepository.existsById(musicPiece.getId())).thenReturn(true);

        musicPieceService.removeMusicPiece(musicPiece.getId(), response);

        Mockito.verify(musicPieceRepository).deleteById(musicPiece.getId());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
