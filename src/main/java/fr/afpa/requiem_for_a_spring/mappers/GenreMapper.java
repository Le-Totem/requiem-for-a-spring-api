package fr.afpa.requiem_for_a_spring.mappers;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.GenreDto;
import fr.afpa.requiem_for_a_spring.entities.Genre;

@Service
public class GenreMapper {
    // ENTITY TO DTO
    public GenreDto convertToDto(Genre genre) {
        return new GenreDto(genre);
    }

    // DTO TO ENTITY
    public Genre convertToEntity(GenreDto genreDto) {
        return new Genre(genreDto);
    }
}