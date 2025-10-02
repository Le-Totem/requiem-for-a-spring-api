package fr.afpa.requiem_for_a_spring.entities;

import java.util.Date;

import fr.afpa.requiem_for_a_spring.dtos.InvitationDto;
import fr.afpa.requiem_for_a_spring.enums.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "invitation")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "invit_status")
    @JdbcType(PostgreSQLEnumJdbcType.class)// Type Hibernate pour PostgreSQL enum
    private Status status;

    @Column(name = "created_at")
    private Date created_at;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_group", nullable = false)
    private Group group;

    public Invitation() {

    }

    public Invitation(InvitationDto invitationDto) {
        this.id = invitationDto.getId();
        this.email = invitationDto.getEmail();
        this.status = invitationDto.getStatus();
        this.created_at = invitationDto.getCreated_at();
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
