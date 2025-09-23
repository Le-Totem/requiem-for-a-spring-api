package fr.afpa.requiem_for_a_spring.mailer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;


@Configuration
public class MailConfig {
    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("requiemforaspring@gmail.com");
        message.setText(
                "This is the test email template for your email:\n%s\n");
        return message;
    }

}
