package fr.afpa.requiem_for_a_spring.web.controllers;

import fr.afpa.requiem_for_a_spring.config.jwt.RequireRole;
import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import fr.afpa.requiem_for_a_spring.dtos.InvitationDto;
import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import fr.afpa.requiem_for_a_spring.entities.MusicPiece;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import fr.afpa.requiem_for_a_spring.services.GroupService;
import fr.afpa.requiem_for_a_spring.services.InvitationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;
    private final MusicPieceRepository musicPieceRepository;
    private final InvitationService invitationService;

    public GroupController(GroupService groupService, MusicPieceRepository musicPieceRepository,
            InvitationService invitationService) {
        this.groupService = groupService;
        this.musicPieceRepository = musicPieceRepository;
        this.invitationService = invitationService;
    }

    /**
     * Requête pour récupérer tous les ensembles existants. ✅
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<List<GroupDto>> getAll() {
        List<GroupDto> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }

    /**
     * Requête pour récupérer un ensemble. ✅
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getById(@PathVariable Integer id) {
        GroupDto group = groupService.findById(id);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(group);
    }

    /**
     * Requête pour récupérer les fiches morceaux d'un ensemble. ✅
     * 
     * @param groupId
     * @return
     */
    @GetMapping("/{groupId}/track")
    public ResponseEntity<List<MusicPieceDto>> getMusicByGroup(@PathVariable Integer groupId) {
        List<MusicPiece> pieces = musicPieceRepository.findAllByGroup_Id(groupId);
        List<MusicPieceDto> dtos = pieces.stream().map(MusicPieceDto::new).toList();

        if (dtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(dtos);
    }

    /**
     * Requête pour récupérer les invitations d'un ensemble ✅
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}/invitations")
    @RequireRole(role = Role.MODERATEUR)
    public ResponseEntity<List<InvitationDto>> getInvitations(@PathVariable Integer id) {
        return new ResponseEntity<>(invitationService.getAllInvitations(id), HttpStatus.OK);
    }

    /**
     * Requête pour créer un ensemble. ✅
     * 
     * @param groupDto
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<GroupDto> create(@RequestBody GroupDto groupDto) {
        GroupDto saved = groupService.save(groupDto);
        System.out.println("DEBUG: POST /api/groups/create => saved id=" + saved.getId());
        return ResponseEntity.status(201).body(saved);
    }

    /**
     * Requête pour inviter un utilisateur dans un ensemble ✅
     */

    @PostMapping("{id_group}/invite_user")
    @RequireRole(role = Role.MODERATEUR)
    public ResponseEntity<InvitationDto> createInvitation(
            @PathVariable Integer id_group,
            @RequestBody InvitationDto invitationDto) {
        InvitationDto saved = invitationService.createInvitation(invitationDto, id_group);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Requête pour supprimer un ensemble. ✅
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @RequireRole(role = Role.ADMIN)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        groupService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
