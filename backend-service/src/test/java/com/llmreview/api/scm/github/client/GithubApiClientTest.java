package com.llmreview.api.scm.github.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.llmreview.api.scm.github.status.GithubNotfoundRepository;
import com.llmreview.api.scm.github.status.GithubUnauthorized;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(properties = "spring.profiles.active=test")
@TestPropertySource(locations = {
    "classpath:application-test.yaml"
})
@EnableAutoConfiguration(exclude = {
    HibernateJpaAutoConfiguration.class,
    DataSourceAutoConfiguration.class
})
@Import(GithubApiClient.class)
class GithubApiClientTest {
    @Autowired
    private GithubApiClient githubApiClient;
    private GithubRepoInfoRequest validRequest;
    private GithubRepoInfoRequest invalidRepositoryRequest;
    private GithubRepoInfoRequest invalidTokenRequest;
    /**
     * 깃헙 API 를 이용해서 레포지토리 정보를 정상적으로 조회하는지 테스트
     * - 유효한 깃헙 레포지토리 정보 요청을 보내고, 응답으로 레포지토리 정보가 올바르게 반환되는지 확인
     * - 유효하지 않은 레포지토리 요청을 보내면 GithubNotfoundRepository 예외가 발생하는지 확인
     * - 유효하지 않은 토큰을 사용한 요청을 보내면 GithubUnauthorized 예외가 발생하는지 확인
     */
    @BeforeEach
    void setup() {
        Dotenv dotenv = Dotenv.configure().filename(".env.test").load();
        String githubOwner = dotenv.get("GITHUB_OWNER");
        String githubRepo = dotenv.get("GITHUB_REPO");
        String githubToken = dotenv.get("GITHUB_TOKEN");
        String githubForbiddenToken = dotenv.get("GITHUB_FORBIDDEN_TOKEN");
        log.debug("Github Dotenv Variables: owner={}, repo={}, token={}, forbiddenToken={}",githubOwner, githubRepo, githubToken, githubForbiddenToken);
        validRequest = new GithubRepoInfoRequest(
            githubOwner,
            githubRepo,
            githubToken
        );
        log.info("GithubApiClientTest.setup - valid request: {}", validRequest);
        invalidRepositoryRequest = new GithubRepoInfoRequest(
            "invalid-owner",
            "invalid-repo",
            githubToken
        );
        log.info("GithubApiClientTest.setup - invalid request: {}", invalidRepositoryRequest);
        invalidTokenRequest = new GithubRepoInfoRequest(
            githubOwner,
            githubRepo,
            "invalid-token"
        );
        log.info("GithubApiClientTest.setup - invalid token request: {}", invalidTokenRequest);
    }

    /**
     * 깃헙 API 를 이용해서 레포지토리 정보를 정상적으로 조회하는지 테스트
     * @throws IOException Http 요청 처리 중 발생할 수 있는 예외
     * @throws InterruptedException 스레드가 인터럽트된 경우 발생할 수 있는 예외
     */
    @Test
    @DisplayName("깃헙 API - 유효한 레포지토리로 레포지토리 정보 요청")
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

    /**
     * 깃헙 API 를 이용해서 유효하지 않은 레포지토리 요청을 보냈을 때, GithubNotfoundRepository 예외가 발생하는지 테스트
     * @throws IOException Http 요청 처리 중 발생할 수 있는 예외
     * @throws InterruptedException 스레드가 인터럽트된 경우 발생할 수 있는 예외
     *
     */
    @Test
    @DisplayName("깃헙 API - 유효하지 않은 레포지토리로 레포지토리 정보 요청 시 GithubNotfoundRepository 예외 발생")
    void testGetRepositoryInfo_invalidRepository() {
        assertThatThrownBy(() -> githubApiClient.getRepositoryInfo(invalidRepositoryRequest))
            .isInstanceOf(GithubNotfoundRepository.class)
            .hasMessageContaining("Repository not found: " + invalidRepositoryRequest.owner() + "/" + invalidRepositoryRequest.repo());
    }

    /**
     * 깃헙 API 를 이용해서 유효하지 않은 토큰을 사용한 요청을 보냈을 때, GithubUnauthorized 예외가 발생하는지 테스트
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    @DisplayName("깃헙 API - 유효하지 않은 토큰으로 레포지토리 정보 요청 시 GithubUnauthorized 예외 발생")
    void testGetRepositoryInfo_invalidToken() {
        assertThatThrownBy(() -> githubApiClient.getRepositoryInfo(invalidTokenRequest))
            .isInstanceOf(GithubUnauthorized.class)
            .hasMessageContaining("Unauthorized access to repository: " + invalidTokenRequest.owner() + "/" + invalidTokenRequest.repo());

        // Note: The actual exception type may vary based on the implementation of the GithubApiClient.
        // Adjust the assertion as necessary to match the expected behavior.
    }

}