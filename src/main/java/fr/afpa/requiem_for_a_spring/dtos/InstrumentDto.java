package fr.afpa.requiem_for_a_spring.dtos;

import fr.afpa.requiem_for_a_spring.entities.Instrument;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class InstrumentDto {

    private Integer id;
    private String name;
    private Set<MediaInstrumentDto> mediaInstruments = new HashSet<>();

    public InstrumentDto() {}

    public InstrumentDto(Integer id, String name, Set<MediaInstrumentDto> mediaInstruments) {
        this.id = id;
        this.name = name;
        this.mediaInstruments = mediaInstruments;
    }

    public InstrumentDto(Instrument instrument) {
        this.id = instrument.getId();
        this.name = instrument.getName();
        this.mediaInstruments = instrument.getMediaInstruments().stream()
                .map(mi -> new MediaInstrumentDto(
                        mi.getMedia().getId(),
                        mi.getMedia().getTitle(),
                        mi.getInstrument().getId(),
                        mi.getInstrument().getName()
                ))
                .collect(Collectors.toSet());
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

    public Set<MediaInstrumentDto> getMediaInstruments() {
        return mediaInstruments;
    }

    public void setMediaInstruments(Set<MediaInstrumentDto> mediaInstruments) {
        this.mediaInstruments = mediaInstruments;
    }
}
