package com.llmreview.api.scm.github.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.llmreview.api.scm.github.status.GithubNotfoundRepository;
import com.llmreview.api.scm.github.status.GithubUnauthorized;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubApiClient {
    private final ObjectMapper objectMapper;

    public JsonNode getRepositoryInfo(GithubRepoInfoRequest githubRequest) throws IOException, InterruptedException {
        String url = String.format("https://api.github.com/repos/%s/%s", githubRequest.owner(), githubRequest.repo());
        log.debug("github client request url : {}", url);
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + githubRequest.token())
            .header("Accept", "application/vnd.github+json")
            .GET()
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.debug("github client response code : {}", response.statusCode());
        if (response.statusCode() == 401) {
            throw new GithubUnauthorized(githubRequest.owner(), githubRequest.repo());
        } else if (response.statusCode() == 403) {
            log.debug("Forbidden access to repository {}:{}", githubRequest.owner(), githubRequest.repo());
            throw new RuntimeException("Access forbidden to the repository. Please check your permissions.");
        } else if (response.statusCode() == 404) {
            throw new GithubNotfoundRepository(githubRequest.owner(), githubRequest.repo());
        }
        return this.objectMapper.readTree(response.body());

    }
}
