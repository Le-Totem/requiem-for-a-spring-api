package fr.afpa.requiem_for_a_spring.config.jwt;

import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final UserRepository userRepository;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                        UserRepository userRepository) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.userRepository = userRepository;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        AuthenticationProvider authenticationProvider) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(auth -> auth
                                                // Routes publiques
                                                .requestMatchers("/api/auth/**").permitAll()
                                                .requestMatchers("/api/users/reset-password").permitAll()
                                                .requestMatchers("/api/**").authenticated()
                                                
                                                // Admin global
                                                .requestMatchers(HttpMethod.DELETE, "/api/groups/**").hasRole("ADMIN")

                                                // Bloquer GET /api/users/** pour les utilisateurs sauf modo et admin
                                                .requestMatchers(HttpMethod.GET, "/api/users/me")
                                                .hasAnyRole("UTILISATEUR",
                                                                "MODERATEUR", "ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/api/users/**")
                                                .hasAnyRole("MODERATEUR",
                                                                "ADMIN")

                                                // Modérateur / Admin global pour le reste des endpoints API
                                                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("UTILISATEUR",
                                                                "MODERATEUR", "ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/groups/create").authenticated()
                                                .requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole("MODERATEUR",
                                                                "ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/**")
                                                .hasAnyRole("MODERATEUR", "ADMIN")
                                                .requestMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("MODERATEUR",
                                                                "ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("MODERATEUR",
                                                                "ADMIN")

                                                // Tout le reste → autorisé
                                                .anyRequest().permitAll())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint((request, response, authException) -> {
                                                        response.setStatus(401);
                                                        response.setContentType("application/json");
                                                        response.getWriter().write(
                                                                        "{\"error\":\"Unauthorized\",\"message\":\""
                                                                                        + authException.getMessage()
                                                                                        + "\"}");
                                                }));

                return http.build();
        }

        // CORS
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("http://localhost:5173"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                configuration.setAllowedHeaders(List.of("*"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        // UserDetailsService
        @Bean
        UserDetailsService userDetailsService() {
                return username -> userRepository.findByEmail(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        // PasswordEncoder
        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // AuthenticationProvider
        @Bean
        AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder);
                return authProvider;
        }

        // AuthenticationManager
        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}
