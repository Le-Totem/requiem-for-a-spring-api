package fr.afpa.requiem_for_a_spring.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.GenreDto;
import fr.afpa.requiem_for_a_spring.entities.Genre;
import fr.afpa.requiem_for_a_spring.entities.MusicPiece;
import fr.afpa.requiem_for_a_spring.entities.MusicPieceGenre;
import fr.afpa.requiem_for_a_spring.entities.MusicPieceGenreId;
import fr.afpa.requiem_for_a_spring.mappers.GenreMapper;
import fr.afpa.requiem_for_a_spring.repositories.GenreRepository;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceGenreRepository;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final MusicPieceRepository musicPieceRepository;
    private final MusicPieceGenreRepository musicPieceGenreRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository, MusicPieceRepository musicPieceRepository,
            MusicPieceGenreRepository musicPieceGenreRepository,
            GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.musicPieceRepository = musicPieceRepository;
        this.musicPieceGenreRepository = musicPieceGenreRepository;
        this.genreMapper = genreMapper;
    }

    /**
     * Récupère tous les genres de la BDD
     * 
     * @return Une liste de genres
     */
    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll().stream().map(genre -> new GenreDto(genre)).collect(Collectors.toList());
    }

    /**
     * Récupère le ou les genres d'une fiche morceau
     * 
     * @param id L'id de la fiche morceau
     * @return Une liste d'un ou plusieurs genres
     */
    public List<GenreDto> getAllGenresByIdMusicPiece(Integer id) {
        return genreRepository.findAllByMusicPieceGenre_Genre_Id(id).stream().map(genre -> new GenreDto(genre))
                .collect(Collectors.toList());
    }

    /**
     * Crée un genre
     * 
     * @param genreDto
     * @return
     */
    public GenreDto createGenre(GenreDto genreDto) {
        Genre genre = genreMapper.convertToEntity(genreDto);
        genre = genreRepository.save(genre);
        genreDto.setId(genre.getId());
        return genreDto;
    }

    /**
     * Ajouter une liste de genres à une fiche morceau
     */
    public List<GenreDto> addGenres(Integer id, List<GenreDto> genres) {
        MusicPiece musicPiece = musicPieceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Morceau introuvable."));
        List<GenreDto> genresToAdd = new ArrayList<GenreDto>();

        for (GenreDto genreDto : genres) {
            Genre genre = genreRepository.findById(genreDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Genre introuvable."));
            MusicPieceGenreId musicPieceGenreId = new MusicPieceGenreId(musicPiece.getId(), genre.getId());
            if (!musicPieceGenreRepository.existsById(musicPieceGenreId)) {
                MusicPieceGenre musicPieceGenre = new MusicPieceGenre(musicPiece, genre);
                musicPieceGenreRepository.save(musicPieceGenre);
            }
            genresToAdd.add(new GenreDto(genre));
        }

        return genresToAdd;
    }
}
