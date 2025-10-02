package fr.afpa.requiem_for_a_spring.entities;

import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "\"group\"")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "is_everyone_admin", nullable = false)
    private Boolean isEveryoneAdmin = false;

    @OneToMany(mappedBy = "group")
    private List<MusicPiece> musicPieces;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Invitation> invitations;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<UserGroup>  userGroups;
    public Group() {

    }

    public Group(GroupDto groupDto) {

        this.id = groupDto.getId();
        this.name = groupDto.getName();
        this.creationDate = groupDto.getCreationDate();
        this.isEveryoneAdmin = groupDto.getIsEveryoneAdmin();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getIsEveryoneAdmin() {
        return isEveryoneAdmin;
    }

    public void setIsEveryoneAdmin(Boolean isEveryoneAdmin) {
        this.isEveryoneAdmin = isEveryoneAdmin;
    }

    public List<MusicPiece> getMusicPieces() {
        return musicPieces;
    }

    public void setMusicPieces(List<MusicPiece> musicPieces) {
        this.musicPieces = musicPieces;
    }

    public Boolean getEveryoneAdmin() {
        return isEveryoneAdmin;
    }

    public void setEveryoneAdmin(Boolean everyoneAdmin) {
        isEveryoneAdmin = everyoneAdmin;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }
}