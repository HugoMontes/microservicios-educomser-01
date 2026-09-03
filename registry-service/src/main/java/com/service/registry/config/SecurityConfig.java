package com.service.registry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.eureka.username}")
    private String username;

    @Value("${app.eureka.password}")
    private String password;

    /**
     * Configura la autenticación en memoria con usuarios.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username(username)
                .password(passwordEncoder().encode(password)) // Encripta la contraseña
                .roles("USER") // roles() añade prefijo "ROLE_"
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Configura la codificación de contraseñas (BCrypt es el estándar).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura las reglas de seguridad HTTP.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Deshabilita CSRF (API REST)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated()   // Todas las peticiones requieren autenticación
                )
                .httpBasic(Customizer.withDefaults());  // Habilita autenticación básica HTTP

        return http.build();
    }
}