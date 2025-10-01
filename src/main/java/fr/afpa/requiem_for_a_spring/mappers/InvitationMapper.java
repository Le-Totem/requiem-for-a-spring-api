package fr.afpa.requiem_for_a_spring.mappers;

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
}
