package fr.afpa.requiem_for_a_spring.entities;

import fr.afpa.requiem_for_a_spring.dtos.MediaDto;
import fr.afpa.requiem_for_a_spring.enums.MediaType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_media", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "mediatype")
    private MediaType type;

    @Column(name = "url", length = 100)
    private String url;

    @Column(name = "date_added", nullable = false)
    private LocalDate dateAdded;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_track", nullable = false)
    private MusicPiece idTrack;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private User idUser;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MediaInstrument> mediaInstruments = new HashSet<>();

    public Media(MediaDto mediaDto) {
    }

    public Media() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public MusicPiece getIdTrack() {
        return idTrack;
    }

    public void setIdTrack(MusicPiece idTrack) {
        this.idTrack = idTrack;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public Set<MediaInstrument> getMediaInstruments() {
        return mediaInstruments;
    }

    public void setMediaInstruments(Set<MediaInstrument> mediaInstruments) {
        this.mediaInstruments = mediaInstruments;
    }
}
