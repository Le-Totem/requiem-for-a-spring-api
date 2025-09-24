package fr.afpa.requiem_for_a_spring.entities;

import java.io.Serializable;
import java.util.Objects;

public class MediaInstrumentId implements Serializable {
    private Integer media;
    private Integer instrument;

    public MediaInstrumentId() {}

    public MediaInstrumentId(Integer media, Integer instrument) {
        this.media = media;
        this.instrument = instrument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaInstrumentId)) return false;
        MediaInstrumentId that = (MediaInstrumentId) o;
        return Objects.equals(media, that.media) &&
                Objects.equals(instrument, that.instrument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(media, instrument);
    }

    public Integer getMedia() {
        return media;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }

    public Integer getInstrument() {
        return instrument;
    }

    public void setInstrument(Integer instrument) {
        this.instrument = instrument;
    }
}
