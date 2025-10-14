package fr.afpa.requiem_for_a_spring.dtos;

public class MediaInstrumentDto {

    private Integer mediaId;
    private String mediaTitle;
    private Integer instrumentId;
    private String instrumentName;

    public MediaInstrumentDto() {}

    public MediaInstrumentDto(Integer mediaId, String mediaTitle, Integer instrumentId, String instrumentName) {
        this.mediaId = mediaId;
        this.mediaTitle = mediaTitle;
        this.instrumentId = instrumentId;
        this.instrumentName = instrumentName;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public Integer getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Integer instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }
}
