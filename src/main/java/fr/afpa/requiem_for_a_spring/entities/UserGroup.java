package fr.afpa.requiem_for_a_spring.entities;

import fr.afpa.requiem_for_a_spring.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class UserGroup {
    @EmbeddedId
    private UserGroupId id;

    @ManyToOne
    @MapsId("id_user")
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @MapsId("id_group")
    @JoinColumn(name = "id_group")
    private Group group;

    @Column(name = "role", nullable = false)
    private Role role;

    public UserGroup(UserGroupId id, User user, Group group, Role role) {
        this.id = new UserGroupId(user.getId(), group.getId());
        this.user = user;
        this.group = group;
        this.role = role;
    }

    public UserGroupId getId() {
        return id;
    }

    public void setId(UserGroupId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
