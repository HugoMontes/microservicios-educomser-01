package com.api.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity // Habilita seguridad reactiva para WebFlux (Gateway)
public class SecurityConfig {

    // Bean principal que define la cadena de filtros de seguridad.
    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity http) {
        return http
                // Desabilitar CSRF porque el gateway funciona como API stateless
                // y no usa sesiones ni formularios
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // Configuramos autorizacion de peticiones HTTP
                .authorizeExchange(exchanges -> exchanges
                        // Permitimos acceso sin autenticación a los endpoints de Eureka
                        .pathMatchers("/eureka/**").permitAll()
                        // Cualquier otra petición debe estar autenticada
                        .anyExchange().authenticated()
                )
                // Configuramos el Gateway como Resource Server OAuth2
                // para que valide tokens JWT enviados en cada petición
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults())
                )
                .build();
    }
}

