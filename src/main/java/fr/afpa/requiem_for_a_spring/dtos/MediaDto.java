package fr.afpa.requiem_for_a_spring.dtos;

import fr.afpa.requiem_for_a_spring.entities.Media;

import fr.afpa.requiem_for_a_spring.enums.MediaType;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MediaDto {
    private Integer id;
    private MediaType type;
    private String url;
    private LocalDate dateAdded;
    private LocalDate dateModified;
    private Integer trackId;
    private UUID userId;
    private Set<Integer> instrumentIds;

    public MediaDto() {}

    public MediaDto(Media media) {
        this.id = media.getId();
        this.type = media.getType();
        this.url = media.getUrl();
        this.dateAdded = media.getDateAdded();
        this.dateModified = media.getDateModified();
        this.trackId = media.getIdTrack() != null ? media.getIdTrack().getId() : null;
        this.userId = media.getIdUser() != null ? UUID.fromString(media.getIdUser().getId().toString()) : null;

        this.instrumentIds = media.getMediaInstruments().stream()
                .map(mi -> mi.getInstrument().getId())
                .collect(Collectors.toSet());
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

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Set<Integer> getInstrumentIds() {
        return instrumentIds;
    }

    public void setInstrumentIds(Set<Integer> instrumentIds) {
        this.instrumentIds = instrumentIds;
    }
}
