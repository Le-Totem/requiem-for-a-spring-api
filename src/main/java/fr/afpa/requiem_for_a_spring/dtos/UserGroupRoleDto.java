package fr.afpa.requiem_for_a_spring.dtos;

import fr.afpa.requiem_for_a_spring.enums.Role;

public record UserGroupRoleDto(Integer groupId, Role role) {
}