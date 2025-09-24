package fr.afpa.requiem_for_a_spring.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.afpa.requiem_for_a_spring.entities.MusicPiece;

public class MusicPieceDto {
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("author")
    private String author;
    @JsonProperty("description")
    private String description;
    @JsonProperty("id_group")
    private Integer id_group;

    public MusicPieceDto() {

    }

    // constructeur pour le mapper DTO > Entity
    public MusicPieceDto(MusicPiece musicPiece) {
        this.id = musicPiece.getId();
        this.title = musicPiece.getTitle();
        this.author = musicPiece.getAuthor();
        this.description = musicPiece.getDescription();
        if (musicPiece.getGroup() != null) {
            this.id_group = musicPiece.getGroup().getId();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId_group() {
        return id_group;
    }

    public void setId_group(Integer id_group) {
        this.id_group = id_group;
    }

}
