package com.dragi.finance_manager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    public String issuerUri;

    @Value("${keycloak.client-id}")
    public String clientName;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());

        boolean keycloakReady = false;
        int retryAttempts = 5;
        int delay = 20000;

        for (int i = 0; i < retryAttempts; i++) {
            try {
                LOGGER.info("Attempting to configure JWT decoder with Keycloak issuer URI: {}", issuerUri);
                http.oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(JwtDecoders.fromIssuerLocation(issuerUri))
                                .jwtAuthenticationConverter(customJwtAuthenticationConverter())));
                keycloakReady = true;
                break;
            } catch (Exception e) {
                LOGGER.warn("Keycloak not ready yet. Retrying in {} seconds... (Attempt {}/{})", delay / 1000, i + 1, retryAttempts);
                Thread.sleep(delay);
            }
        }

        if (!keycloakReady) {
            throw new IllegalStateException("Unable to connect to Keycloak after " + retryAttempts + " attempts.");
        }

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/**").permitAll()
                .requestMatchers("/api/transactions/**").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter customJwtAuthenticationConverter() {
        LOGGER.info("Creating custom JWT Authentication Converter...");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(customJwtGrantedAuthoritiesConverter());
        LOGGER.info("Custom JWT Authentication Converter created.");
        return converter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> customJwtGrantedAuthoritiesConverter() {
        LOGGER.info("Initializing custom JwtGrantedAuthoritiesConverter with client name: {}", clientName);
        return new KeycloakRoleConverter(clientName);
    }
}
