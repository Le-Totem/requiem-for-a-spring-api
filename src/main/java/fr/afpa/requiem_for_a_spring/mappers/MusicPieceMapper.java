package fr.afpa.requiem_for_a_spring.mappers;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.entities.MusicPiece;

@Service
public class MusicPieceMapper {
    // ENTITY TO DTO
    public MusicPieceDto convertToDto(MusicPiece musicPiece) {
        return new MusicPieceDto(musicPiece);
    }

    // DTO TO ENTITY
    public MusicPiece convertToEntity(MusicPieceDto musicPieceDto) {
        MusicPiece musicPiece = new MusicPiece(musicPieceDto);

        if (musicPieceDto.getId_group() != null) {
            Group group = new Group();
            group.setId(musicPieceDto.getId_group());
            musicPiece.setGroup(group);
        }
        return musicPiece;
    }
}
