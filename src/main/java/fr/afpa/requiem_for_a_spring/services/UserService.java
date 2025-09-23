package fr.afpa.requiem_for_a_spring.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.dtos.UserRoleDto;
import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.entities.UserGroup;
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

    public UserService(UserRepository userRepository, UserMapper userMapper, UserGroupRepository userGroupRepository) {
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
    public List<UserDto> getAllUsersByIdGroup(Integer id) {
        return userRepository.findAllByUserGroups_Group_Id(id).stream().map(user -> new UserDto(user))
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

        // Retourne une erreur si l'id ne correspond à aucun id en BDD
        if (!id.equals(userDto.getId())) {
            throw new IllegalArgumentException("L'id ne correspond à aucun utilisateur.");
        }

        User user = originalUser.get();
        return userMapper.convertToDto(userRepository.save(user));
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
        User user = userRepository.findById(id_user).orElse(null);

        UserGroup userGroup = userGroupRepository.findByUser_IdAndGroup_Id(userRoleDto.getId_user(),
                userRoleDto.getId_group());
        userGroup.setRole(userRoleDto.getRole());

        return new UserDto(user);
    }

    /**
     * Supprime un utilisateur
     * 
     * @param id       L'id de l'utilisateur à supprimer
     * @param response Réponse HTTP renvoyée
     */
    public void removeMusicPiece(UUID id, HttpServletResponse response) {
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
