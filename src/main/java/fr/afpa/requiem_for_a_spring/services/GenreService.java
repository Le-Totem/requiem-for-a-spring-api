package fr.afpa.requiem_for_a_spring.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.GenreDto;
import fr.afpa.requiem_for_a_spring.entities.Genre;
import fr.afpa.requiem_for_a_spring.mappers.GenreMapper;
import fr.afpa.requiem_for_a_spring.repositories.GenreRepository;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
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
        return genreRepository.findAllByIdMusicPiece(id).stream().map(genre -> new GenreDto(genre))
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
}
