package fr.afpa.requiem_for_a_spring;

import fr.afpa.requiem_for_a_spring.dtos.MediaDto;
import fr.afpa.requiem_for_a_spring.entities.*;
import fr.afpa.requiem_for_a_spring.enums.MediaType;
import fr.afpa.requiem_for_a_spring.mappers.MediaMapper;
import fr.afpa.requiem_for_a_spring.repositories.InstrumentRepository;
import fr.afpa.requiem_for_a_spring.repositories.MediaRepository;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import fr.afpa.requiem_for_a_spring.services.MediaService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MediaServiceTest {

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private MediaMapper mediaMapper;

    @Mock
    private MusicPieceRepository musicPieceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InstrumentRepository instrumentRepository;

    @InjectMocks
    private MediaService mediaService;

    @Test
    public void testFindAll() {
        Mockito.when(mediaRepository.findAll()).thenReturn(Arrays.asList(new Media()));
        List<MediaDto> media = mediaService.getAll();
        assertEquals(1, media.size());
    }

    @Test
    public void testFindById() {
        // Arrange
        Media media = new Media();
        media.setId(1);
        media.setType(MediaType.PDF);

        when(mediaRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(media));

        // Act
        MediaDto result = mediaService.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(MediaType.PDF, result.getType());

        verify(mediaRepository).findById(1);
    }

    @Test
    public void testFindMediasByMusicPieceId() {
        Media media = new Media();
        media.setId(1);
        media.setTitle("Partition piano");

        MusicPiece musicPiece = new MusicPiece();
        musicPiece.setId(1);

        List<Media> medias = Arrays.asList(media);

        when(mediaRepository.findByIdTrack_Id(musicPiece.getId())).thenReturn(medias);

        List<MediaDto> result = mediaService.getMediasByIdMusicPiece(musicPiece.getId());

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(mediaRepository).findByIdTrack_Id(musicPiece.getId());
    }

    @Test
    @Transactional
    public void testCreateMedia() {
        // Arrange
        UUID userId = UUID.randomUUID(); // simulate un vrai UUID

        MediaDto dto = new MediaDto();
        dto.setType(MediaType.PDF);
        dto.setTrackId(1);
        dto.setUserId(userId);
        dto.setInstrumentIds(Set.of(3));

        MusicPiece track = new MusicPiece();
        track.setId(1);

        User user = new User();
        user.setId(userId);

        Instrument instrument = new Instrument();
        instrument.setId(3);

        Media savedEntity = new Media();
        savedEntity.setId(10);
        savedEntity.setType(MediaType.PDF);
        savedEntity.setIdTrack(track);
        savedEntity.setIdUser(user);

        // Mocks
        when(musicPieceRepository.findById(1)).thenReturn(Optional.of(track));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(instrumentRepository.findById(3)).thenReturn(Optional.of(instrument));
        when(mediaRepository.save(any(Media.class))).thenReturn(savedEntity);

        // Act
        MediaDto result = mediaService.createMedia(dto);

        // Assert
        assertNotNull(result);
        assertEquals(MediaType.PDF, result.getType());
        assertEquals(10, result.getId());
        assertEquals(userId, result.getUserId());

        verify(musicPieceRepository).findById(1);
        verify(userRepository).findById(userId);
        verify(instrumentRepository).findById(3);
        verify(mediaRepository, atLeastOnce()).save(any(Media.class));
    }

    @Test
    public void testDelete() {
        // Arrange
        Media media = new Media();
        media.setId(1);

        Mockito.when(mediaRepository.findById(1)).thenReturn(Optional.of(media));

        // Act
        mediaService.deleteById(1);

        // Assert
        Mockito.verify(mediaRepository).findById(1);
        Mockito.verify(mediaRepository).delete(media);

    }

}
