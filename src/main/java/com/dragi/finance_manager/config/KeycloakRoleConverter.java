package com.dragi.finance_manager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakRoleConverter.class);

    public static final String REALM_ACCESS = "realm_access";
    public static final String RESOURCE_ACCESS = "resource_access";
    private final String clientName;

    public KeycloakRoleConverter(String clientName) {
        this.clientName = clientName;
        LOGGER.info("KeycloakJwtTokenConverter initialized with client name: {}", clientName);
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        LOGGER.info("Converting JWT token with ID: {}", jwt.getId());
        Collection<GrantedAuthority> finalRoles = new ArrayList<>();

        // Extract realm roles
        extractRealmRoles(jwt.getClaim(REALM_ACCESS), finalRoles);

        // Extract client roles
        extractClientRoles(jwt, finalRoles);

        LOGGER.info("Converted JWT token ID: {} with roles: {}", jwt.getId(), finalRoles);
        return finalRoles;
    }

    private void extractRealmRoles(Object realmAccess, Collection<GrantedAuthority> finalRoles) {
        LOGGER.info("Extracting realm roles from JWT...");
        if (realmAccess instanceof Map) {
            Map<String, List<String>> rolesMap = (Map<String, List<String>>) realmAccess;
            List<String> roles = rolesMap.getOrDefault("roles", new ArrayList<>());
            roles.forEach(role -> {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
                finalRoles.add(authority);
                LOGGER.info("Added realm role: {}", authority);
            });
        } else {
            LOGGER.warn("Realm roles not found in JWT.");
        }
    }

    private void extractClientRoles(Jwt jwt, Collection<GrantedAuthority> finalRoles) {
        LOGGER.info("Extracting client roles for client: {}", clientName);
        Map<String, Map<String, List<String>>> resourceAccessMap = jwt.getClaim(RESOURCE_ACCESS);

        if (resourceAccessMap != null && resourceAccessMap.containsKey(clientName)) {
            Map<String, List<String>> clientRoles = resourceAccessMap.get(clientName);
            List<String> roles = clientRoles.getOrDefault("roles", new ArrayList<>());
            roles.forEach(role -> {
                GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
                finalRoles.add(authority);
                LOGGER.info("Added client role: {}", authority);
            });
        } else {
            LOGGER.warn("Client roles not found for client: {}", clientName);
        }
    }
}