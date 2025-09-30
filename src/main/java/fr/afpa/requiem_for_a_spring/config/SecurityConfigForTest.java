package fr.afpa.requiem_for_a_spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Permissions pour les tests unitaires
@Configuration
@Profile("test")
public class SecurityConfigForTest {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // désactive CSRF pour tests API
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // autorise tout (GET/POST/PATCH/DELETE...)
                );

        return http.build();
    }
}
