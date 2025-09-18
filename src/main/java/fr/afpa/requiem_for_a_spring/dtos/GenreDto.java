package fr.afpa.requiem_for_a_spring.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.afpa.requiem_for_a_spring.entities.Genre;

public class GenreDto {
    private Integer id;
    @JsonProperty("name")
    private String name;

    public GenreDto() {

    }

    // constructeur pour le mapper DTO > Entity
    public GenreDto(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
