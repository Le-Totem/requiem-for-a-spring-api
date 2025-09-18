package fr.afpa.requiem_for_a_spring.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.afpa.requiem_for_a_spring.entities.Group;

import java.time.LocalDate;

public class GroupDto {
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("creation_date")
    private LocalDate creationDate;

    @JsonProperty("is_everyone_admin")
    private Boolean isEveryoneAdmin;

    public GroupDto() {

    }

    // Constructeur pour mapper Entity -> DTO
    public GroupDto(Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.creationDate = group.getCreationDate();
        this.isEveryoneAdmin = group.getIsEveryoneAdmin();
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
