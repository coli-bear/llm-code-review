package com.llmreview.gateway.filter;

import com.llmreview.gateway.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "com.llmreview.jwt.secret-key=12345678901234567890123456789012",
        "com.llmreview.jwt.issuer=llmreview.com",
        "logging.level.com.llmreview.gateway=DEBUG"
    }
)
class JwtAuthFilterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private JwtUtil jwtUtil;
    private final String validUserToken = "valid.user.token";
    private final String validAdminToken = "valid.admin.token";
    private final String invalidToken = "invalid.jwt.token";

    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";

    @BeforeEach
    void setUp() {
        when(jwtUtil.validateToken(validUserToken)).thenReturn(true);
        when(jwtUtil.extractRole(validUserToken)).thenReturn(USER_ROLE);

        when(jwtUtil.validateToken(validAdminToken)).thenReturn(true);
        when(jwtUtil.extractRole(validAdminToken)).thenReturn(ADMIN_ROLE);

        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);
    }

    @Test
    void shouldReturn401WhenAuthorizationHeaderIsMissing() {
        webTestClient.get()
            .uri("/v1/api/test")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void shouldReturn401WhenTokenIsInvalid() {
        webTestClient.get()
            .uri("/v1/api/test")
            .header("Authorization", "Bearer " + invalidToken)
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void shouldReturn401WhenAuthorizationHeaderIsMalformed() {
        webTestClient.get()
            .uri("/v1/api/test")
            .header("Authorization", "Bearer malformed.token")
            .exchange()
            .expectStatus()
            .isUnauthorized();
    }

    @Test
    void shouldPassWhenTokenIsValid() {
        webTestClient.get()
            .uri("/v1/api/test-connection")
            .header("Authorization", "Bearer " + validUserToken)
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @Test
    void shouldReturn403WhenRoleIsNotUser() {
        webTestClient.get()
            .uri("/v1/api/test-connection")
            .header("Authorization", "Bearer " + validAdminToken)
            .exchange()
            .expectStatus().isForbidden();
    }
}