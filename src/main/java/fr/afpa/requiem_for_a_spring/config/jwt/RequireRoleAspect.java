package fr.afpa.requiem_for_a_spring.config.jwt;

import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.enums.Role;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Aspect
@Component
public class RequireRoleAspect {

    private final PermissionService permissionService;

    public RequireRoleAspect(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint joinPoint, RequireRole requireRole) {
        // Rôle attendu
        Role requiredRole = requireRole.role();

        // Utilisateur courant (depuis Spring Security)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // On cherche l'integer du groupe dans les arguments de la méthode
        Integer id_group = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Integer) {
                id_group = (Integer) arg;
                break;
            }
        }

        if (id_group == null) {
            throw new IllegalStateException("Impossible de trouver un id_group dans les paramètres de la méthode.");
        }

        // Vérifie les permissions
        if (!permissionService.hasRoleInGroup(user, id_group, requiredRole)) {
            throw new ResponseStatusException(FORBIDDEN,
                    "L'utilisateur " + user.getEmail() + " n'a pas le rôle " + requiredRole + " dans le groupe "
                            + id_group);
        }
    }
}
