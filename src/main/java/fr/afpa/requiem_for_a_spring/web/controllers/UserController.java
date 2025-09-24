package fr.afpa.requiem_for_a_spring.web.controllers;

import java.util.List;
import java.util.UUID;

import fr.afpa.requiem_for_a_spring.dtos.InviteUserDto;
import fr.afpa.requiem_for_a_spring.services.InvitMembreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.dtos.UserRoleDto;
import fr.afpa.requiem_for_a_spring.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    private final InvitMembreService invitMembreService;

    public UserController(UserService userService, InvitMembreService invitMembreService) {
        this.userService = userService;
        this.invitMembreService = invitMembreService;
    }

    /**
     * Requête pour récupérer tous les utilisateurs ✅
     * 
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Requête pour récupérer un utilisateur en fonction de son id ✅
     * 
     * 
     * @param id L'id de l'utilisateur à récupérer
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOneUserById(@PathVariable UUID id) {
        return new ResponseEntity<>(userService.getOneUserById(id), HttpStatus.OK);
    }

    /**
     * Requête pour récupérer les utilisateurs d'un ensemble ✅
     * 
     * @param id L'id de l'ensemble
     * @return
     */
    @GetMapping("/group/{id}")
    public ResponseEntity<List<UserDto>> getAllUsersByIdGroup(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getAllUsersByIdGroup(id), HttpStatus.OK);
    }

    /**
     * Requête pour modifier un utilisateur ✅
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
     * Requête pour inviter un utilisateur dans un ensemble
     */
    @PostMapping("/invite")
    public ResponseEntity<UserDto> inviteUser(@RequestBody InviteUserDto request) {
        try {
            UserDto invited = invitMembreService.inviteUserToGroup(
                    request.getEmail(),
                    request.getGroup(),
                    request.getRole());
            return new ResponseEntity<>(invited, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Requête pour modifier le rôle d'un utilisateur dans un ensemble ✅
     * 
     * @param id_user     L'id de l'utilisateur
     * @param id_group    L'id de l'ensemble
     * @param userRoleDto
     * @return Un utilisateur mis à jour
     */
    @PatchMapping("/group/{id_group}/user/{id_user}")
    public ResponseEntity<UserDto> updateRoleUser(@PathVariable Integer id_group, @PathVariable UUID id_user,
            @RequestBody UserRoleDto userRoleDto) {
        try {
            // La requête a réussi et le rôle de l'utilisateur a été modifié
            return new ResponseEntity<>(userService.updateUserRole(id_user, id_group, userRoleDto), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // La requête a échoué, l'utilisateur et/ou le group n'ont pas été trouvé +
            // erreur 404
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Requête pour supprimer un utilisateur ✅
     * 
     * @param id       L'id de l'utilisateur à supprimer
     * @param response Réponse HTTP renvoyée
     */
    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable UUID id, HttpServletResponse response) {
        userService.removeMusicPiece(id, response);
    }
}
