package fr.afpa.requiem_for_a_spring.entities;

import java.io.Serializable;

import jakarta.persistence.Column;

public class MusicPieceGenreId implements Serializable {

    @Column(name = "id_music_piece")
    private Integer id_music_piece;

    @Column(name = "id_genre")
    private Integer id_genre;

    public MusicPieceGenreId() {
    }

    public MusicPieceGenreId(Integer id_music_piece, Integer id_genre) {
        this.id_music_piece = id_music_piece;
        this.id_genre = id_genre;
    }

}
