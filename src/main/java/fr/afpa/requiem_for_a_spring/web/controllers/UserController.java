package fr.afpa.requiem_for_a_spring.web.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import fr.afpa.requiem_for_a_spring.config.jwt.RequireRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import fr.afpa.requiem_for_a_spring.dtos.InvitationDto;
import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.dtos.UserGroupRoleDto;
import fr.afpa.requiem_for_a_spring.dtos.UserInfoDto;
import fr.afpa.requiem_for_a_spring.dtos.UserRoleDto;
import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.services.InvitationService;
import fr.afpa.requiem_for_a_spring.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private InvitationService invitationService;

    public UserController(UserService userService, InvitationService invitationService) {
        this.invitationService = invitationService;
        this.userService = userService;
    }

    /**
     * Requête pour récupérer tous les utilisateurs ✅
     * 
     * @return
     */
    @GetMapping("/all")
    @RequireRole(role = Role.MODERATEUR)
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
    @RequireRole(role = Role.MODERATEUR)
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
    @RequireRole(role = Role.MODERATEUR)
    public ResponseEntity<List<UserDto>> getAllUsersByIdGroup(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getAllUsersByIdGroup(id), HttpStatus.OK);
    }

    /**
     * Récupère les infos de l'utilisateur (notamment les rôles) pour le front
     * 
     * @param user
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        UserInfoDto userInfo = new UserInfoDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getUserGroups()
                        .stream()
                        .map(ug -> new UserGroupRoleDto(ug.getId().getId_group(), ug.getRole()))
                        .collect(Collectors.toList()));

        return ResponseEntity.ok(userInfo);
    }

    /**
     * Requête pour modifier un utilisateur ✅
     * 
     * @param id      L'id de l'utilisateur à modifier
     * @param userDto L'utilisateur à modifier
     * @return L'utilisateur modifié
     */
    @PatchMapping("/{id}")
    @RequireRole(role = Role.MODERATEUR)
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
     * Endpoint pour réinitialiser le mot de passe d'un utilisateur.
     * 
     * 
     * Cette méthode accepte une requête POST contenant l'email de l'utilisateur
     * et le nouveau mot de passe. Elle permet de réinitialiser directement le mot
     * de passe sans que l'utilisateur soit connecté.
     *
     * @param body Map contenant les champs "email" et "newPassword"
     * @return ResponseEntity avec un message de succès ou d'erreur
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String newPassword = body.get("newPassword");

        try {
            userService.resetPasswordByEmail(email, newPassword);
            return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Utilisateur non trouvé"));
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
    @RequireRole(role = Role.ADMIN)
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
     * Requête pour avoir toutes les invitation d'un utilisateur
     * 
     * @param email email de l'utilisateur connecté
     * @return Un utilisateur mis à jour
     */
    @GetMapping("/email/{email}")
    public List<InvitationDto> getInvitationsByEmail(@PathVariable String email) {
        return invitationService.amIInvited(email);
    }


    /**
     * Endpoint pour envoyer un code à un utilisateur
     * 
     * @param body Contient "email" et "code"
     * @return Message de succès ou d'erreur
     */
    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String codeStr = body.get("code");

        if (email == null || codeStr == null) {
            return ResponseEntity.badRequest().body("Email et code requis");
        }

        try {
            Integer code = Integer.parseInt(codeStr);
            invitationService.sendCode(code, email);
            return ResponseEntity.ok("Code envoyé avec succès");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Code invalide");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'envoi du code : " + e.getMessage());
        }
    }


        /**
     * Requête pour supprimer un utilisateur ✅
     * 
     * @param id       L'id de l'utilisateur à supprimer
     * @param response Réponse HTTP renvoyée
     */
    @DeleteMapping("/{id}")
    @RequireRole(role = Role.ADMIN)
    public void removeUser(@PathVariable UUID id, HttpServletResponse response) {
        userService.deleteUser(id, response);
    }
}
