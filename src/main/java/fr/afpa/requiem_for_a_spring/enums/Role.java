package fr.afpa.requiem_for_a_spring.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("ADMIN")
    ADMIN,
    @JsonProperty("MODERATEUR")
    MODERATEUR,
    @JsonProperty("UTILISATEUR")
    UTILISATEUR;
}
