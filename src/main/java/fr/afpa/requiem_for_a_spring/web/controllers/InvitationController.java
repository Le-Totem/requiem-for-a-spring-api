
package fr.afpa.requiem_for_a_spring.web.controllers;

import fr.afpa.requiem_for_a_spring.dtos.InvitationDto;
import fr.afpa.requiem_for_a_spring.services.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("/create/{id_group}")
    public ResponseEntity<InvitationDto> createInvitation(
            @RequestBody InvitationDto dto,
            @PathVariable Integer id_group) {
        InvitationDto invitation = invitationService.createInvitation(dto, id_group);
        return ResponseEntity.ok(invitation);
    }
}
