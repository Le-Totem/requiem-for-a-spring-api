package fr.afpa.requiem_for_a_spring.entities;

import java.io.Serializable;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGroupId))
            return false;
        UserGroupId that = (UserGroupId) o;
        return Objects.equals(id_user, that.id_user) &&
                Objects.equals(id_group, that.id_group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_user, id_group);
    }
}
