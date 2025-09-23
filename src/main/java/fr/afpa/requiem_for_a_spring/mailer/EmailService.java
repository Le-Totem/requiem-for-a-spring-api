package fr.afpa.requiem_for_a_spring.mailer;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
