package fr.afpa.requiem_for_a_spring.entities;

import java.util.List;

import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import jakarta.persistence.*;
// import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "music_piece")
public class MusicPiece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "author")
    private String author;

    @Column(nullable = true, name = "description")
    private String description;

    // relation avec Genre
    @OneToMany(mappedBy = "music_piece", targetEntity = Genre.class)
    private List<Genre> genres;

    @ManyToOne
    @JoinColumn(name = "id_group")
    private Group group;

    // // relation avec Media
    // @ManyToOne
    // @JoinColumn(name = "id_media")
    // private Media media;

    public MusicPiece() {

    }

    // constructeur pour le mapper Entity > DTO
    public MusicPiece(MusicPieceDto musicPieceDto) {
        this.id = musicPieceDto.getId();
        this.title = musicPieceDto.getTitle();
        this.author = musicPieceDto.getAuthor();
        this.description = musicPieceDto.getDescription();
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

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    // public Media getMedia() {
    // return media;
    // }

    // public void setMedia(Media media) {
    // this.media = media;
    // }

}
