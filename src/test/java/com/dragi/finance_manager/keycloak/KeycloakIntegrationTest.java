package com.dragi.finance_manager.keycloak;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KeycloakIntegrationTest {

    private static final String KEYCLOAK_URL = "http://localhost:9090/admin/master/console/";

    @Test
    void testKeycloakIsRunning() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(KEYCLOAK_URL, String.class);
            assertThat(response).isNotNull();
        } catch (Exception e) {
            assertThat(false).isTrue();
        }
    }
}
