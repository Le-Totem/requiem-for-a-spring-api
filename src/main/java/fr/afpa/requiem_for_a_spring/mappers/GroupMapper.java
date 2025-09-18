package fr.afpa.requiem_for_a_spring.mappers;

import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import org.springframework.stereotype.Service;

@Service
public class GroupMapper {
    // ENTITY TO DTO
    public GroupDto convertToDto(Group group) {

        return new GroupDto(group);
    }

    // DTO TO ENTITY
    public Group convertToEntity(GroupDto groupDto) {
        return new Group(groupDto);
    }
}
