package fr.afpa.requiem_for_a_spring.services;

import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.entities.UserGroup;
import fr.afpa.requiem_for_a_spring.entities.UserGroupId;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.mailer.EmailServiceImpl;
import fr.afpa.requiem_for_a_spring.repositories.UserGroupRepository;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class InvitMembreService {

    private final EmailServiceImpl emailService;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    public InvitMembreService(EmailServiceImpl emailService, UserRepository userRepository,
            UserGroupRepository userGroupRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
    }

    public UserDto inviteUserToGroup(String email, Group group, Role role) {
        // Vérifie si utilisateur existe déjà
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // Si utilisateur n'existe pas"
            user = new User();
            user.setEmail(email);
            user.setIs_validated(false);
            user = userRepository.save(user);
        }

        // Vérifie que groupe existe
        UserGroupId userGroupId = new UserGroupId(user.getId(), group.getId());
        UserGroup userGroup = userGroupRepository.findById(userGroupId).orElse(null);
        if (userGroup == null) {
            userGroup = new UserGroup(user, group, role);
            userGroupRepository.save(userGroup);
        } else {
            // Utilisateur est déjà dans le groupe
        }

        // TODO : envoyer un email d'invitation avec un lien
        emailService.sendSimpleMessage(email,
                "Mail d'invitation a l'ensemble" + userGroup.getGroup().getName(),
                "Bonjour, pour rejoindre l'ensemble cliqué sur ce lien ");

        return new UserDto(user);
    }
}
