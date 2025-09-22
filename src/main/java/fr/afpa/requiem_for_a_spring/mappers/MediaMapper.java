package fr.afpa.requiem_for_a_spring.mappers;


import fr.afpa.requiem_for_a_spring.dtos.MediaDto;
import fr.afpa.requiem_for_a_spring.entities.Media;

public class MediaMapper {    // ENTITY TO DTO
    public MediaDto convertToDto(Media media) {

        return new MediaDto(media);
    }

    // DTO TO ENTITY
    public Media convertToEntity(MediaDto mediaDto) {
        return new Media(mediaDto);
    }
}
