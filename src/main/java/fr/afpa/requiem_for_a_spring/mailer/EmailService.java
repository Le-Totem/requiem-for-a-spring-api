package fr.afpa.requiem_for_a_spring.mailer;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}

