package fr.afpa.requiem_for_a_spring.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.entities.Invitation;
import fr.afpa.requiem_for_a_spring.enums.Status;

public class InvitationDto {
    private Integer id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("status")
    private Status status;
    @JsonProperty("created_at")
    private Date created_at;
    @JsonIgnore
    private Group id_group;

    public InvitationDto() {

    }

    public InvitationDto(Invitation invitation) {
        this.id = invitation.getId();
        this.email = invitation.getEmail();
        this.status = invitation.getStatus();
        this.created_at = invitation.getCreated_at();
        this.id_group = invitation.getId_group();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Group getId_group() {
        return id_group;
    }

    public void setId_group(Group id_group) {
        this.id_group = id_group;
    }

}
