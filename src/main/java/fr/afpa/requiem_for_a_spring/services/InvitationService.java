package fr.afpa.requiem_for_a_spring.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.InvitationDto;
import fr.afpa.requiem_for_a_spring.repositories.InvitationRepository;

@Service
public class InvitationService {
    private InvitationRepository invitationRepository;

    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    /**
     * Récupère toutes les invations d'un ensemble
     * 
     * @param id_group
     * @return
     */
    public List<InvitationDto> getAllInvitations(Integer id_group) {
        return invitationRepository.findById_Group(id_group)
                .stream()
                .map(invitation -> new InvitationDto(invitation))
                .collect(Collectors.toList());
    }

    /**
     * TODO: Créer une invitation
     */
}
