package fr.afpa.requiem_for_a_spring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MediaInstrumentId implements Serializable {
    private static final long serialVersionUID = -6497901065804540255L;
    @Column(name = "id_media", nullable = false)
    private Integer idMedia;

    @Column(name = "id_instrument", nullable = false)
    private Integer idInstrument;

    public MediaInstrumentId() {

    }

    public MediaInstrumentId(Integer id, Integer id1) {
    }

    public Integer getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(Integer idMedia) {
        this.idMedia = idMedia;
    }

    public Integer getIdInstrument() {
        return idInstrument;
    }

    public void setIdInstrument(Integer idInstrument) {
        this.idInstrument = idInstrument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MediaInstrumentId entity = (MediaInstrumentId) o;
        return Objects.equals(this.idInstrument, entity.idInstrument) &&
                Objects.equals(this.idMedia, entity.idMedia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInstrument, idMedia);
    }

}