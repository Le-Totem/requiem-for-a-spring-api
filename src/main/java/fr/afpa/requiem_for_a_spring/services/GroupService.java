package fr.afpa.requiem_for_a_spring.services;

import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import fr.afpa.requiem_for_a_spring.dtos.UserRoleDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.entities.UserGroup;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.mappers.GroupMapper;
import fr.afpa.requiem_for_a_spring.repositories.GroupRepository;
import fr.afpa.requiem_for_a_spring.repositories.UserGroupRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UserGroupRepository userGroupRepository;

    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper,
            UserGroupRepository userGroupRepository) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.userGroupRepository = userGroupRepository;
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

    public List<UserRoleDto> findMyGroups() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        List<UserGroup> userGroups = userGroupRepository.findByUser_Id(currentUser.getId());

        return userGroups.stream()
                .map(ug -> {
                    UserRoleDto dto = new UserRoleDto();
                    dto.setId_user(currentUser.getId());
                    dto.setId_group(ug.getGroup().getId());
                    dto.setRole(ug.getRole());
                    dto.setGroup(groupMapper.convertToDto(ug.getGroup()));
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public GroupDto save(GroupDto dto) {
        // Récupère l'utilisateur courant depuis le SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        // Sauvegarde l'ensemble
        Group entity = groupMapper.convertToEntity(dto);
        Group savedGroup = groupRepository.save(entity);

        // Link le créateur à l'ensemble
        UserGroup userGroup = new UserGroup(currentUser, savedGroup, Role.ADMIN);
        userGroupRepository.save(userGroup);

        return groupMapper.convertToDto(savedGroup);
    }
    public GroupDto update(Integer groupId, String newName) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Groupe non trouvé"));

        group.setName(newName);

        Group updatedGroup = groupRepository.save(group);

        return groupMapper.convertToDto(updatedGroup);
    }


    public void deleteById(Integer id) {
        groupRepository.deleteById(id);
    }
}
