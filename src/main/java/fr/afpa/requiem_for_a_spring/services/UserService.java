package fr.afpa.requiem_for_a_spring.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.mappers.UserMapper;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
        return userRepository.findAllUsersByIdGroup(id).stream().map(user -> new UserDto(user))
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

    // updateRoleUser(UUID id, UserDto userDto) + Role Dto

    // id_user
    // id_group
    // role
    // public UserDto updateUserRole(UUID id_user, UserDto userDto, UserRoleDto
    // userRoleDto) {
    // Optional<User> originalUser = userRepository.findById(id_user);
    // UserGroup userGroup = userRepository.findUserGroupByIdUser(id_user);

    // userGroup.setRole(userRoleDto.getRole());
    // userRepository.save(null)

    // }

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
