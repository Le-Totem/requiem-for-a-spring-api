package fr.afpa.requiem_for_a_spring.config.jwt;

import fr.afpa.requiem_for_a_spring.enums.Role;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    Role role();

    // Dans les controllers
    // @RequireRole(role = Role.ADMIN)
    // @RequireRole(role = Role.MODERATEUR)
    // @RequireRole(role = Role.UTILISATEUR)
}
