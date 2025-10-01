package fr.afpa.requiem_for_a_spring.config.jwt;

import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
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
    private final UserRepository userRepository;

    public RequireRoleAspect(PermissionService permissionService, UserRepository userRepository) {
        this.permissionService = permissionService;
        this.userRepository = userRepository;
    }

    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint joinPoint, RequireRole requireRole) {
        System.out.println(">>> RequireRoleAspect déclenché sur " + joinPoint.getSignature());

        Role requiredRole = requireRole.role();
        System.out.println(">>> Rôle requis : " + requiredRole);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(">>> Authentication = " + authentication);

        Object principal = authentication.getPrincipal();
        System.out.println(">>> Principal = " + principal + " (" + principal.getClass() + ")");

        User user;
        if (principal instanceof User) {
            user = (User) principal;
        } else if (principal instanceof String email) {
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
        } else {
            throw new IllegalStateException("Type de principal inattendu : " + principal.getClass());
        }

        // Vérifie si la méthode a un paramètre id_group
        Integer id_group = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof Integer) {
                id_group = (Integer) arg;
                break;
            }
        }

        if (id_group != null) {
            // Vérification du rôle dans un groupe spécifique
            if (!permissionService.hasRoleInGroup(user, id_group, requiredRole)) {
                throw new ResponseStatusException(FORBIDDEN,
                        "L'utilisateur " + user.getEmail() + " n'a pas le rôle " + requiredRole
                                + " dans le groupe " + id_group);
            }
        } else {
            // Vérification globale (par exemple ADMIN global, MODO global, etc.)
            boolean hasRequiredRole = user.getUserGroups().stream()
                    .anyMatch(userGroup -> permissionService.hasAtLeastRole(userGroup.getRole(), requiredRole));

            if (!hasRequiredRole) {
                throw new ResponseStatusException(FORBIDDEN,
                        "L'utilisateur " + user.getEmail() + " n'a pas le rôle global " + requiredRole);
            }

        }
    }
}
