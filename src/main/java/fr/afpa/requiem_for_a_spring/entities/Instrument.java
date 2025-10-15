package fr.afpa.requiem_for_a_spring.entities;

import fr.afpa.requiem_for_a_spring.dtos.InstrumentDto;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "instrument")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "instrument", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MediaInstrument> mediaInstruments = new HashSet<>();

    public Instrument(InstrumentDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.mediaInstruments = new HashSet<>();
    }

    public Instrument() {

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

    public Set<MediaInstrument> getMediaInstruments() {
        return mediaInstruments;
    }

    public void setMediaInstruments(Set<MediaInstrument> mediaInstruments) {
        this.mediaInstruments = mediaInstruments;
    }
}