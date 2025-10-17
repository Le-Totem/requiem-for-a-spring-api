package fr.afpa.requiem_for_a_spring.web.controllers;

import fr.afpa.requiem_for_a_spring.config.jwt.RequireRole;
import fr.afpa.requiem_for_a_spring.dtos.InvitationDto;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.mailer.EmailServiceImpl;
import fr.afpa.requiem_for_a_spring.services.InvitationService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailServiceImpl emailService;
    private final InvitationService invitationService;

    public EmailController(EmailServiceImpl emailService, InvitationService invitationService) {
        this.emailService = emailService;
        this.invitationService = invitationService;
    }

    @GetMapping("/send-email")
    @RequireRole(role = Role.MODERATEUR)
    public String sendEmail() {
        emailService.sendSimpleMessage("bidoo974@outlook.com", "Mail de test", "Bonjour ceci est un test");
        return "Mail envoyé";
    }

    @GetMapping("/email/{email}")
    public List<InvitationDto> getInvitationsByEmail(@PathVariable String email) {
        return invitationService.amIInvited(email);
    }
}
