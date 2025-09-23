package fr.afpa.requiem_for_a_spring.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "media_instrument")
public class MediaInstrument {
    @EmbeddedId
    private MediaInstrumentId id;

    @MapsId("idMedia")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_media", nullable = false)
    private Media media;

    @MapsId("idInstrument")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_instrument", nullable = false)
    private Instrument instrument;

    public MediaInstrumentId getId() {
        return id;
    }

    public void setId(MediaInstrumentId id) {
        this.id = id;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument idInstrument) {
        this.instrument = idInstrument;
    }

}