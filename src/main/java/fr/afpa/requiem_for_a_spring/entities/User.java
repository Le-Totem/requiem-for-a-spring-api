package fr.afpa.requiem_for_a_spring.entities;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import jakarta.persistence.*;

@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "firstname")
    private String firstname;

    @Column(nullable = false, name = "lastname")
    private String lastname;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "is_validated")
    private Boolean is_validated = false;

    @Column(nullable = true, name = "picture")
    private String picture;

    @OneToMany(mappedBy = "idUser", targetEntity = Media.class)
    private List<Media> medias;

    @OneToMany(mappedBy = "user", targetEntity = UserGroup.class)
    private List<UserGroup> userGroups;

    public User() {

    }

    // constructeur Entity > DTO
    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.firstname = userDto.getFirstname();
        this.lastname = userDto.getLastname();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.is_validated = userDto.getIs_validated();
        this.picture = userDto.getPicture();
    }

    // JWT

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userGroups.stream()
                .map(role -> "ROLE_" + role.getRole().name())
                .map(roleName -> (GrantedAuthority) () -> roleName)
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    // getters & setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIs_validated() {
        return is_validated;
    }

    public void setIs_validated(Boolean is_validated) {
        this.is_validated = is_validated;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }
}
