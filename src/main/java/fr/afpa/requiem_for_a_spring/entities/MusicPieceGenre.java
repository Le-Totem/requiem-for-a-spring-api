package fr.afpa.requiem_for_a_spring.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "music_piece_genre")
public class MusicPieceGenre {

    @EmbeddedId
    private MusicPieceGenreId id;

    @ManyToOne
    @MapsId("id_music_piece")
    @JoinColumn(name = "id_track", nullable = false)
    private MusicPiece musicPiece;

    @ManyToOne
    @MapsId("id_genre")
    @JoinColumn(name = "id_genre", nullable = false)
    private Genre genre;

    public MusicPieceGenre() {
    }

    public MusicPieceGenre(MusicPiece musicPiece, Genre genre) {
        this.id = new MusicPieceGenreId(musicPiece.getId(), genre.getId());
        this.musicPiece = musicPiece;
        this.genre = genre;
    }

    public MusicPieceGenreId getId() {
        return id;
    }

    public void setId(MusicPieceGenreId id) {
        this.id = id;
    }
    
    public MusicPiece getMusicPiece() {
        return musicPiece;
    }

    public void setMusicPiece(MusicPiece musicPiece) {
        this.musicPiece = musicPiece;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }


}
