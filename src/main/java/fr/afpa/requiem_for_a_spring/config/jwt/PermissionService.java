package fr.afpa.requiem_for_a_spring.config.jwt;

import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.entities.UserGroup;
import fr.afpa.requiem_for_a_spring.entities.UserGroupId;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.repositories.UserGroupRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    private final UserGroupRepository userGroupRepository;

    public PermissionService(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    /**
     * Vérifie si un utilisateur a un rôle donné dans un groupe
     */
    public boolean hasRoleInGroup(User user, Integer id_group, Role role) {
        UserGroupId userGroupId = new UserGroupId(user.getId(), id_group);
        return userGroupRepository.findById(userGroupId)
                .map(UserGroup::getRole)
                .map(r -> r == role || (role == Role.UTILISATEUR && r != null))
                .orElse(false);
    }

    /**
     * Lève une exception si l'utilisateur n'a pas le rôle attendu
     */
    public void checkRoleInGroup(User user, Integer id_group, Role role) {
        if (!hasRoleInGroup(user, id_group, role)) {
            throw new AccessDeniedException(
                    String.format("L'utilisateur %s n'a pas le rôle %s dans le groupe %s",
                            user.getEmail(), role, id_group));
        }
    }
}
