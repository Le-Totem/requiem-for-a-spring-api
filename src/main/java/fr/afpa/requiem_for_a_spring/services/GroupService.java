package fr.afpa.requiem_for_a_spring.services;

import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.mappers.GroupMapper;
import fr.afpa.requiem_for_a_spring.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    public List<GroupDto> findAll() {
        return groupRepository.findAll()
                .stream()
                .map(groupMapper::convertToDto)
                .collect(Collectors.toList());
    }

    public GroupDto findById(Integer id) {
        return groupRepository.findById(id)
                .map(groupMapper::convertToDto)
                .orElse(null);
    }

    public GroupDto save(GroupDto dto) {
        Group entity = groupMapper.convertToEntity(dto);
        return groupMapper.convertToDto(groupRepository.save(entity));
    }

    public void deleteById(Integer id) {
        groupRepository.deleteById(id);
    }
}
