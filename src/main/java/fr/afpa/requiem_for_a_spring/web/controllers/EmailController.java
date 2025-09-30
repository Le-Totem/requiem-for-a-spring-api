package fr.afpa.requiem_for_a_spring.web.controllers;

import fr.afpa.requiem_for_a_spring.config.jwt.RequireRole;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.mailer.EmailServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailServiceImpl emailService;

    public EmailController(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send-email")
    @RequireRole(role = Role.MODERATEUR)
    public String sendEmail() {
        emailService.sendSimpleMessage("bidoo974@outlook.com", "Mail de test", "Bonjour ceci est un test");
        return "Mail envoyé";
    }
}
