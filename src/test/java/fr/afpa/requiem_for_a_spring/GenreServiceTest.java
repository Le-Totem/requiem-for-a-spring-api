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

import fr.afpa.requiem_for_a_spring.dtos.GenreDto;
import fr.afpa.requiem_for_a_spring.entities.Genre;
import fr.afpa.requiem_for_a_spring.entities.MusicPiece;
import fr.afpa.requiem_for_a_spring.entities.MusicPieceGenre;
import fr.afpa.requiem_for_a_spring.mappers.GenreMapper;
import fr.afpa.requiem_for_a_spring.repositories.GenreRepository;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceGenreRepository;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import fr.afpa.requiem_for_a_spring.services.GenreService;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {
    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @Mock
    private MusicPieceGenreRepository musicPieceGenreRepository;

    @Mock
    private MusicPieceRepository musicPieceRepository;

    @InjectMocks
    private GenreService genreService;

    @Test
    public void testFindAll() {
        List<Genre> genreList = Arrays.asList(new Genre());
        Mockito.when(genreRepository.findAll()).thenReturn(genreList);

        List<GenreDto> genres = genreService.getAllGenres();
        assertEquals(1, genres.size());
    }

    @Test
    public void testFindAllGenresByIdMusicPiece() {
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Rap");

        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setName("Rap");

        MusicPiece musicPiece = new MusicPiece();
        musicPiece.setId(1);
        musicPiece.setTitle("Bella Ciao");

        MusicPieceGenre musicPieceGenre = new MusicPieceGenre(musicPiece, genre);

        List<MusicPieceGenre> musicPieceGenres = Arrays.asList(musicPieceGenre);

        Mockito.when(musicPieceRepository.existsById(1)).thenReturn(true);
        Mockito.when(musicPieceGenreRepository.findAllByMusicPiece_Id(1))
                .thenReturn(musicPieceGenres);

        List<GenreDto> result = genreService.getAllGenresByIdMusicPiece(1);

        assertEquals(1, result.size());
        assertEquals("Rap", result.get(0).getName());
    }

    @Test
    public void testCreateGenre() {
        Genre genre = new Genre();
        genre.setName("Rap");

        GenreDto genreDto = new GenreDto();
        genreDto.setName("Rap");

        Genre savedGenre = new Genre();
        savedGenre.setId(1);
        savedGenre.setName("Rap");

        Mockito.when(genreMapper.convertToEntity(genreDto)).thenReturn(genre);
        Mockito.when(genreRepository.save(genre)).thenReturn(savedGenre);

        GenreDto result = genreService.createGenre(genreDto);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        assertEquals("Rap", result.getName());

        Mockito.verify(genreMapper).convertToEntity(genreDto);
        Mockito.verify(genreRepository).save(genre);
    }

    @Test
    public void testAddGenres() {
        Integer musicPieceId = 1;

        MusicPiece musicPiece = new MusicPiece();
        musicPiece.setId(musicPieceId);
        musicPiece.setTitle("Bella Ciao");

        Genre genre = new Genre();
        genre.setId(10);
        genre.setName("Rap");

        GenreDto genreDto = new GenreDto();
        genreDto.setId(10);
        genreDto.setName("Rap");

        Mockito.when(musicPieceRepository.findById(musicPieceId))
                .thenReturn(Optional.of(musicPiece));

        Mockito.when(genreRepository.findById(10))
                .thenReturn(Optional.of(genre));

        Mockito.when(musicPieceGenreRepository.existsById(Mockito.any()))
                .thenReturn(false);

        List<GenreDto> result = genreService.addGenres(musicPieceId, Arrays.asList(genreDto));

        assertEquals(1, result.size());
        assertEquals("Rap", result.get(0).getName());
        assertEquals(10, result.get(0).getId());

        Mockito.verify(musicPieceRepository).findById(musicPieceId);
        Mockito.verify(genreRepository).findById(10);
        Mockito.verify(musicPieceGenreRepository).save(Mockito.any(MusicPieceGenre.class));
    }
}
