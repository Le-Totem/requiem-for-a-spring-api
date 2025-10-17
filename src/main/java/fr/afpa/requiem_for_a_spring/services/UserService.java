package fr.afpa.requiem_for_a_spring.services;

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

    public UserService(UserRepository userRepository, UserMapper userMapper,
            UserGroupRepository userGroupRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userGroupRepository = userGroupRepository;
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

    public UserDto inscriptionValid(UUID id, UserDto userDto){
        Optional<User> originalUser = userRepository.findById(id);

        if (originalUser.isEmpty()) {
            throw new EntityNotFoundException("L'utilisateur' est introuvable.");
        }

         User user = originalUser.get();

         user.setIs_validated(true);
        
        return userDto;
        
    }

    /**
     * Endpoint pour réinitialiser le mot de passe d'un utilisateur.
     * 
     * ette méthode accepte une requête POST contenant l'email de l'utilisateur
     * et le nouveau mot de passe. Elle permet de réinitialiser directement le mot
     * de passe sans que l'utilisateur soit connecté.
     *
     * @param body Map contenant les champs "email" et "newPassword"
     * @return ResponseEntity avec un message de succès ou d'erreur
     */
    public void resetPasswordByEmail(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        user.setPassword(passwordEncoder.encode(newPassword));
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
