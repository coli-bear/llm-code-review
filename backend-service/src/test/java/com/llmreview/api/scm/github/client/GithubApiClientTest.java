package com.llmreview.api.scm.github.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.llmreview.api.scm.github.status.GithubException;
import com.llmreview.api.scm.github.status.GithubNotfoundRepository;
import com.llmreview.api.scm.github.status.GithubUnauthorized;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@TestPropertySource(locations = {
    "classpath:application-test.yaml"
})
class GithubApiClientTest {
    private final GithubApiClient githubApiClient = new GithubApiClient(new ObjectMapper());

    public static Stream<Arguments> validRequestResourceProvider() {
        Dotenv dotenv = Dotenv.configure().filename(".env.test").load();
        String githubOwner = dotenv.get("GITHUB_OWNER");
        String githubPublicRepo = dotenv.get("GITHUB_PUBLIC_REPO");
        String githubPublicToken = dotenv.get("GITHUB_PUBLIC_REPO_TOKEN");
        return Stream.of(
            Arguments.of(new GithubClientRequestInfo(githubOwner, githubPublicRepo, githubPublicToken))
        );
    }

    public static Stream<Arguments> inValidRequestResourceProvider() {
        Dotenv dotenv = Dotenv.configure().filename(".env.test").load();
        String githubOwner = dotenv.get("GITHUB_OWNER");
        String githubPublicRepo = dotenv.get("GITHUB_PUBLIC_REPO");
        String githubPublicRepoToken = dotenv.get("GITHUB_PUBLIC_REPO_TOKEN");
        return Stream.of(
            Arguments.of(
                new GithubClientRequestInfo(githubOwner, "invalidRepo", githubPublicRepoToken),
                GithubNotfoundRepository.class,
                String.format("Repository not found: %s/invalidRepo", githubOwner)
            ),
            Arguments.of(
                new GithubClientRequestInfo(githubOwner, githubPublicRepo, "invalidToken"),
                GithubUnauthorized.class,
                String.format("Unauthorized access to repository: %s/%s", githubOwner, githubPublicRepo)
            )

        );
    }
    /**
     *
     */
    @ParameterizedTest
    @MethodSource("validRequestResourceProvider")
    @DisplayName("깃헙 API - 유효한 레포지토리로 레포지토리 정보 요청")
    void testGetRepositoryInfo(GithubClientRequestInfo requestInfo) throws IOException, InterruptedException {
        log.debug("Valid Request Info: {}", requestInfo);
        // When
        JsonNode repositoryInfo = githubApiClient.request(requestInfo);
        log.info("Repository Info: {}", repositoryInfo);

        // Then
        assertNotNull(repositoryInfo);
        assertEquals(requestInfo.owner(), repositoryInfo.get("owner").get("login").asText());
        assertEquals(requestInfo.repo(), repositoryInfo.get("name").asText());
    }

    /**
     * 깃헙 API - 유효하지 않는 요청에 대해서 예외 발생 테스트
     *
     * @param requestInfo 요청 정보
     * @param exception   발생할 예외 클래스
     * @param message     예외 메시지
     * @see GithubApiClientTest#inValidRequestResourceProvider()
     */
    @ParameterizedTest
    @MethodSource("inValidRequestResourceProvider")
    @DisplayName("깃헙 API - 유효하지 않은 레포지토리로 레포지토리 정보 요청 시 GithubNotfoundRepository 예외 발생")
    void testGetRepositoryInfo_invalidRepository(
        GithubClientRequestInfo requestInfo,
        Class<? extends GithubException> exception,
        String message
    ) {
        log.debug("Invalid Request Info: {}", requestInfo);
        log.debug("Exception: {}", exception);
        log.debug("Expected Message: {}", message);

        assertThatThrownBy(() -> githubApiClient.request(requestInfo))
            .isInstanceOf(exception)
            .hasMessageContaining(message);
    }
}