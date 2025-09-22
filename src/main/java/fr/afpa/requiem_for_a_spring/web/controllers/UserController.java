package fr.afpa.requiem_for_a_spring.web.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Requête pour récupérer tous les utilisateurs
     * 
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Requête pour récupérer un utilisateur en fonction de son id
     * 
     * @param id L'id de l'utilisateur à récupérer
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOneUserById(@PathVariable UUID id) {
        return new ResponseEntity<>(userService.getOneUserById(id), HttpStatus.OK);
    }

    /**
     * Requête pour récupérer les utilisateurs d'un ensemble
     * 
     * @param id L'id de l'ensemble
     * @return
     */
    @GetMapping("/group/{id}")
    public ResponseEntity<List<UserDto>> getAllUsersByIdGroup(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getAllUsersByIdGroup(id), HttpStatus.OK);
    }

    /**
     * Requête pour modifier un utilisateur
     * 
     * @param id      L'id de l'utilisateur à modifier
     * @param userDto L'utilisateur à modifier
     * @return L'utilisateur modifié
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id,
            @RequestBody UserDto userDto) {
        try {
            // La requête a réussi et l'utilisateur' a été modifié
            return new ResponseEntity<>(userService.updateUser(id, userDto), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // La requête a échoué, l'utilisateur n'a pas été trouvé + erreur 404
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Requête pour supprimer un utilisateur
     * 
     * @param id       L'id de l'utilisateur à supprimer
     * @param response Réponse HTTP renvoyée
     */
    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable UUID id, HttpServletResponse response) {
        userService.removeMusicPiece(id, response);
    }
}
