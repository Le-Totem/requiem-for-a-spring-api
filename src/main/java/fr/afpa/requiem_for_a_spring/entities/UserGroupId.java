package fr.afpa.requiem_for_a_spring.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserGroupId implements Serializable {
    @Column(name = "id_user")
    private UUID id_user;

    @Column(name = "id_group")
    private Integer id_group;

    public UserGroupId() {

    }

    public UserGroupId(UUID id_user, Integer id_group) {
        this.id_user = id_user;
        this.id_group = id_group;
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

}
