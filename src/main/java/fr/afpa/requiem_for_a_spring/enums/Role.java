package fr.afpa.requiem_for_a_spring.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("Admin")
    ADMIN,
    @JsonProperty("Moderateur")
    MODERATEUR,
    @JsonProperty("Utilisateur")
    UTILISATEUR;
}
