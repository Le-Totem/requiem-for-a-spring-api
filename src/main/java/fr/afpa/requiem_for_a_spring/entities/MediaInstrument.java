package fr.afpa.requiem_for_a_spring.entities;

import jakarta.persistence.*;

@Entity
@IdClass(MediaInstrumentId.class)
@Table(name = "media_instrument")
public class MediaInstrument {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_media", nullable = false)
    private Media media;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_instrument", nullable = false)
    private Instrument instrument;

    public MediaInstrument() {}

    public MediaInstrument(Media media, Instrument instrument) {
        this.media = media;
        this.instrument = instrument;
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

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }
}
