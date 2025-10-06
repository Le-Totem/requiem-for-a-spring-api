package fr.afpa.requiem_for_a_spring.dtos;

import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.enums.Role;

@Service
public class UserRoleDto {
    private UUID id_user;
    private Integer id_group;
    private Role role;
    private GroupDto group;

    public UserRoleDto() {

    }

    public UUID getId_user() {
        return id_user;
    }

    public void setId_user(UUID id_user) {
        this.id_user = id_user;
    }

    public Integer getId_group() {
        return id_group;
    }

    public void setId_group(Integer id_group) {
        this.id_group = id_group;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public GroupDto getGroup() {
        return group;
    }

    public void setGroup(GroupDto group) {
        this.group = group;
    }
}
