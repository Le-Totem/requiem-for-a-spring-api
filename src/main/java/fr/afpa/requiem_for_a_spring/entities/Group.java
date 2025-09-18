package fr.afpa.requiem_for_a_spring.entities;

import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "group")
public class Group {
    @Id
    @Column(name = "id_group", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "is_everyone_admin", nullable = false)
    private Boolean isEveryoneAdmin = false;

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

}