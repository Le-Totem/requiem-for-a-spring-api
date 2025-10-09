package fr.afpa.requiem_for_a_spring.dtos;

import java.util.List;
import java.util.UUID;

public record UserInfoDto(
        UUID id,
        String firstname,
        String lastname,
        String email,
        List<String> roles) {
}
