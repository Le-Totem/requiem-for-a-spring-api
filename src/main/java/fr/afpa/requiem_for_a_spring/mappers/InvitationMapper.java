package fr.afpa.requiem_for_a_spring.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.InvitationDto;
import fr.afpa.requiem_for_a_spring.entities.Invitation;

@Service
public class InvitationMapper {
    // ENTITY TO DO
    public InvitationDto convertToDto(Invitation invitation) {
        return new InvitationDto(invitation);
    }

    // DTO TO ENTITY
    public Invitation convertToEntity(InvitationDto invitationDto) {
        return new Invitation(invitationDto);
    }

    // ENTITY LIST TO DTO LIST
    public List<InvitationDto> toDtoList(List<Invitation> invitations) {
        return invitations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // DTO LIST TO ENTITY LIST (si besoin un jour)
    public List<Invitation> toEntityList(List<InvitationDto> dtos) {
        return dtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }
}
