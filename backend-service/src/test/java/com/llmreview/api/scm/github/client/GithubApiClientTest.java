package com.llmreview.api.scm.github.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.llmreview.api.scm.github.status.GithubNotfoundRepository;
import com.llmreview.api.scm.github.status.GithubUnauthorized;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest(properties = "spring.profiles.active=test")
@TestPropertySource(locations = {
    "classpath:application-test.yaml"
})
@EnableAutoConfiguration(exclude = {
    HibernateJpaAutoConfiguration.class,
    DataSourceAutoConfiguration.class
})
@EnableConfigurationProperties(GithubTestProperties.class)
@Import(GithubApiClient.class)
class GithubApiClientTest {
    @Autowired
    private GithubTestProperties githubTestProperties;

    @Autowired
    private GithubApiClient githubApiClient;
    private GithubRepoInfoRequest validRequest;
    private GithubRepoInfoRequest invalidRepositoryRequest;
    private GithubRepoInfoRequest invalidTokenRequest;

    /**
     * 깃헙 API 를 이용해서 레포지토리 정보를 정상적으로 조회하는지 테스트
     * -
     */
    @BeforeEach
    void setup() {
        Dotenv dotenv = Dotenv.configure().filename(".env.test").load();
        validRequest = new GithubRepoInfoRequest(
            dotenv.get("GITHUB_OWNER"),
            dotenv.get("GITHUB_REPO"),
            dotenv.get("GITHUB_TOKEN")
        );
        log.info("GithubApiClientTest.setup {}", validRequest);
        invalidRepositoryRequest = new GithubRepoInfoRequest(
            "invalid-owner",
            "invalid-repo",
            dotenv.get("GITHUB_TOKEN")
        );
        invalidTokenRequest = new GithubRepoInfoRequest(
            dotenv.get("GITHUB_OWNER"),
            dotenv.get("GITHUB_REPO"),
            "invalid-token"
        );
    }

    @Test
    void testGetRepositoryInfo() throws IOException, InterruptedException {
        // Given

        // When
        JsonNode repositoryInfo = githubApiClient.getRepositoryInfo(validRequest);

        log.info("Repository Info: {}", repositoryInfo);

        // Then
        assertNotNull(repositoryInfo);
        assertEquals("coli-bear", repositoryInfo.get("owner").get("login").asText());
        assertEquals("llm-code-review", repositoryInfo.get("name").asText());
    }

    @Test
    void testGetRepositoryInfo_invalidRepository() throws IOException, InterruptedException {
        assertThatThrownBy(() -> githubApiClient.getRepositoryInfo(invalidRepositoryRequest))
            .isInstanceOf(GithubNotfoundRepository.class)
            .hasMessageContaining("Repository invalid-owner/invalid-repo not found");
    }

    @Test
    void testGetRepositoryInfo_invalidToken() throws IOException, InterruptedException {
        assertThatThrownBy(() -> githubApiClient.getRepositoryInfo(invalidTokenRequest))
            .isInstanceOf(GithubUnauthorized.class)
            .hasMessageContaining("Unautorized access to repository " + invalidTokenRequest.owner() + "/" + invalidTokenRequest.repo());

        // Note: The actual exception type may vary based on the implementation of the GithubApiClient.
        // Adjust the assertion as necessary to match the expected behavior.
    }
}