package fr.afpa.requiem_for_a_spring.web.controllers;

import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import fr.afpa.requiem_for_a_spring.entities.MusicPiece;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import fr.afpa.requiem_for_a_spring.services.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;
    private final MusicPieceRepository musicPieceRepository;

    public GroupController(GroupService groupService, MusicPieceRepository musicPieceRepository) {
        this.groupService = groupService;
        this.musicPieceRepository = musicPieceRepository;
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>> getAll() {
        List<GroupDto> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getById(@PathVariable Integer id) {
        GroupDto group = groupService.findById(id);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(group);
    }

    @GetMapping("/{groupId}/music")
    public ResponseEntity<List<MusicPieceDto>> getMusicByGroup(@PathVariable Integer groupId) {
        List<MusicPiece> pieces = musicPieceRepository.findAllByIdGroup(groupId);
        List<MusicPieceDto> dtos = pieces.stream().map(MusicPieceDto::new).toList();

        if (dtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<GroupDto> create(@RequestBody GroupDto groupDto) {
        GroupDto saved = groupService.save(groupDto);
        return ResponseEntity.status(201).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        groupService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
