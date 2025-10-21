package fr.afpa.requiem_for_a_spring.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.dtos.UserRoleDto;
import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.entities.UserGroup;
import fr.afpa.requiem_for_a_spring.entities.UserGroupId;
import fr.afpa.requiem_for_a_spring.mailer.EmailService;
import fr.afpa.requiem_for_a_spring.mappers.UserMapper;
import fr.afpa.requiem_for_a_spring.repositories.UserGroupRepository;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserGroupRepository userGroupRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

    public UserService(UserRepository userRepository, UserMapper userMapper,
            UserGroupRepository userGroupRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userGroupRepository = userGroupRepository;
        this.emailService = emailService;
    }

    /**
     * Récupère tous les utilisateurs
     * 
     * @return Une liste d'utilisateurs
     */
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new UserDto(user))
                .collect(Collectors.toList());
    }

    /**
     * Récupère un utilisateur en fonction de son id
     * 
     * @param id L'id de l'utilisateur à récupérer
     * @return
     */
    public UserDto getOneUserById(UUID id) {

        return userMapper.convertToDto(userRepository.findById(id).orElse(null));
    }

    /**
     * Récupère les utilisateurs d'un ensemble
     * 
     * @param id_group L'id de l'ensemble
     * @return Une liste de'utilisateurs
     */
    public List<UserDto> getAllUsersByIdGroup(Integer groupId) {
        // Récupère toutes les relations user/group pour cet ensemble
        List<UserGroup> userGroups = userGroupRepository.findByGroup_Id(groupId);

        return userGroups.stream()
                .map(ug -> {
                    UserDto dto = new UserDto(ug.getUser()); // récupère les infos de l'utilisateur
                    dto.setRole(ug.getRole()); // ajoute le rôle dans cet ensemble
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Modifie un utilisateur
     * 
     * @param id      L'id de l'utilisateur à modifier
     * @param userDto L'utilisateur à modifier
     * @return L'utilisateur modifié
     */
    public UserDto updateUser(UUID id, UserDto userDto) {
        Optional<User> originalUser = userRepository.findById(id);

        // Retourne une erreur si l'utilisateur' n'existe pas
        if (originalUser.isEmpty()) {
            throw new EntityNotFoundException("L'utilisateur' est introuvable.");
        }

        User user = originalUser.get();

        if (userDto.getLastname() != null) {
            user.setLastname(userDto.getLastname());
        }
        if (userDto.getFirstname() != null) {
            user.setFirstname(userDto.getFirstname());
        }
        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getIs_validated() != null) {
            user.setIs_validated(userDto.getIs_validated());
        }
        if (userDto.getPicture() != null) {
            user.setPicture(userDto.getPicture());
        }

        return userMapper.convertToDto(userRepository.save(user));
    }

    public UserDto inscriptionValid(UUID id, UserDto userDto) {
        Optional<User> originalUser = userRepository.findById(id);

        if (originalUser.isEmpty()) {
            throw new EntityNotFoundException("L'utilisateur' est introuvable.");
        }

        User user = originalUser.get();

        user.setIs_validated(true);

        return userDto;

    }

    /**
     * 🔐 Envoie un e-mail de réinitialisation de mot de passe à un utilisateur.
     *
     * <p>
     * Cette méthode est appelée lorsqu'un utilisateur indique avoir oublié son mot
     * de passe. Elle génère un jeton unique (UUID) associé à l'utilisateur et une
     * date d'expiration (par défaut 30 minutes). Un lien contenant ce jeton est
     * ensuite envoyé à l'adresse e-mail de l'utilisateur, lui permettant de
     * réinitialiser son mot de passe via le front-end.
     * </p>
     *
     * @param email L’adresse e-mail de l’utilisateur qui a demandé la
     *              réinitialisation.
     * @throws EntityNotFoundException si aucun utilisateur n’existe avec cette
     *                                 adresse e-mail.
     */
    public void sendPasswordResetEmail(String email) {
        // Recherche de l'utilisateur correspondant à l'email fourni
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // Génération d’un token unique et définition de la date d’expiration (30
        // minutes)
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        // Construction du lien de réinitialisation à envoyer par e-mail
        String resetLink = "http://localhost:5173/reset-password?token=" + resetToken;

        // Envoi du mail via le service d’e-mail existant
        emailService.sendSimpleMessage(
                user.getEmail(),
                "Réinitialisation de votre mot de passe",
                "Bonjour " + user.getFirstname() + ",\n\n" +
                        "Cliquez sur ce lien pour réinitialiser votre mot de passe :\n" +
                        resetLink + "\n\n" +
                        "Ce lien expirera dans 30 minutes.\n\n" +
                        "L'équipe Requiem for a Spring.");
    }

    /**
     * Réinitialise le mot de passe d’un utilisateur à partir d’un jeton valide.
     *
     * Cette méthode est appelée lorsque l’utilisateur clique sur le lien reçu par
     * e-mail et soumet un nouveau mot de passe. Elle vérifie que le jeton existe,
     * qu’il n’a pas expiré, puis encode et sauvegarde le nouveau mot de passe avant
     * d’invalider le jeton.
     *
     * @param token       Le jeton unique envoyé par e-mail (paramètre dans l’URL).
     * @param newPassword Le nouveau mot de passe choisi par l’utilisateur.
     * @throws EntityNotFoundException  si le jeton est invalide ou n’existe pas.
     * @throws IllegalArgumentException si le jeton a expiré.
     */
    public void resetPasswordWithToken(String token, String newPassword) {
        // Recherche de l'utilisateur à partir du token
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Lien de réinitialisation invalide"));

        // Vérification de la validité temporelle du lien
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Le lien de réinitialisation a expiré");
        }

        // Mise à jour du mot de passe (après encodage)
        user.setPassword(passwordEncoder.encode(newPassword));

        // Suppression du token et de sa date d’expiration pour empêcher une
        // réutilisation
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    /**
     * Modifie le rôle d'un utilisateur dans un ensemble
     * 
     * @param id_user  L'id de l'utilisateur
     * @param id_group L'id de l'ensemble
     * @param userDto
     * @return Un utilisateur mis à jour
     */
    public UserDto updateUserRole(UUID id_user, Integer id_group, UserRoleDto userRoleDto) {
        UserGroupId userGroupId = new UserGroupId(id_user, id_group);

        UserGroup userGroup = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new EntityNotFoundException("Relation user/group introuvable."));

        userGroup.setRole(userRoleDto.getRole());
        userGroupRepository.save(userGroup);

        return new UserDto(userGroup.getUser());
    }

    /**
     * Supprime un utilisateur
     * 
     * @param id       L'id de l'utilisateur à supprimer
     * @param response Réponse HTTP renvoyée
     */
    public void deleteUser(UUID id, HttpServletResponse response) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            // La requête est réussie et aucune nouvelle information à retourner
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            // La requête a échoué et renvoie une erreur 404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
