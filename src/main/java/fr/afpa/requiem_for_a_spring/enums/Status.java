package fr.afpa.requiem_for_a_spring.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("PENDING")
    PENDING,
    @JsonProperty("ACCEPTED")
    ACCEPTED,
    @JsonProperty("REFUSED")
    REFUSED;
}
