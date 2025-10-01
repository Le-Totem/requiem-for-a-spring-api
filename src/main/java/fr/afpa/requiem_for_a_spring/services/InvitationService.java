package fr.afpa.requiem_for_a_spring.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.entities.Invitation;
import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.enums.Status;
import fr.afpa.requiem_for_a_spring.mailer.EmailServiceImpl;
import fr.afpa.requiem_for_a_spring.repositories.GroupRepository;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.InvitationDto;
import fr.afpa.requiem_for_a_spring.repositories.InvitationRepository;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InvitationService {
    private InvitationRepository invitationRepository;
    private GroupRepository groupRepository;
    private UserRepository userRepository;

    private final EmailServiceImpl emailService;


    public InvitationService(InvitationRepository invitationRepository, GroupRepository groupRepository, UserRepository userRepository, EmailServiceImpl emailService) {
        this.invitationRepository = invitationRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    /**
     * Récupère toutes les invations d'un ensemble
     * 
     * @param id_group
     * @return
     */
    public List<InvitationDto> getAllInvitations(Integer id_group) {
        return invitationRepository.findByGroup_Id(id_group)
                .stream()
                .map(invitation -> new InvitationDto(invitation))
                .collect(Collectors.toList());
    }

    public InvitationDto createInvitation(InvitationDto dto) {
        Group group = groupRepository.findById(dto.getGroup().getId())
                .orElseThrow(() -> new EntityNotFoundException("Groupe introuvable avec id=" + dto.getGroup().getId()));

        // Vérifie si l'utilisateur invité existe déjà
        Optional<User> invitedUser = userRepository.findByEmail(dto.getEmail());

        if (invitedUser.isPresent()) {
            // Utilisateur déjà inscrit
            emailService.sendSimpleMessage(
                    dto.getEmail(),
                    "Invitation à rejoindre le groupe " + group.getName(),
                    "Bonjour, vous avez été invité à rejoindre le groupe '" + group.getName() + "'."
            );
        } else {
            // Utilisateur non inscrit
            emailService.sendSimpleMessage(
                    dto.getEmail(),
                    "Invitation à rejoindre la plateforme",
                    "Bonjour, vous avez été invité à rejoindre le groupe '" + group.getName()
                            + "'. Veuillez créer un compte pour accepter l’invitation."
            );
        }

        Invitation invitation = new Invitation();
        invitation.setEmail(dto.getEmail());
        invitation.setGroup(group);
        invitation.setStatus(Status.PENDING);
        invitation.setCreated_at(new Date());

        invitationRepository.save(invitation);

        return new InvitationDto(invitation);
    }

}
