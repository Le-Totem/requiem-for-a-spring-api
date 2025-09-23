package fr.afpa.requiem_for_a_spring.entities;

import fr.afpa.requiem_for_a_spring.dtos.GenreDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false, name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_track")
    private MusicPiece musicPiece;

    public Genre() {

    }

    // constructeur pour le mapper Entity > DTO
    public Genre(GenreDto genreDto) {
        this.id = genreDto.getId();
        this.name = genreDto.getName();
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

    public MusicPiece getMusicPiece() {
        return musicPiece;
    }

    public void setMusicPiece(MusicPiece musicPiece) {
        this.musicPiece = musicPiece;
    }

}
