package fr.afpa.requiem_for_a_spring.web.controllers;

import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import fr.afpa.requiem_for_a_spring.mappers.MusicPieceMapper;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import fr.afpa.requiem_for_a_spring.services.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<GroupDto> getAll() {
        return groupService.findAll();
    }

    @GetMapping("/{groupId}/music")
    public List<MusicPieceDto> getMusicByGroupId(@PathVariable Integer groupId) {
        return MusicPieceRepository.findAllByIdGroup(groupId)
                .stream()
                .map(MusicPieceMapper::convertToDto)
                .toList();
    }


    @GetMapping("/{id}")
    public GroupDto getById(@PathVariable Integer id) {
        return groupService.findById(id);
    }

    @PostMapping
    public GroupDto create(@RequestBody GroupDto groupDto) {
        return groupService.save(groupDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        groupService.deleteById(id);
    }
}
