package com.llmreview.api.scm.github.client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@Getter
@ActiveProfiles("test")
@ConfigurationProperties(prefix = "github")
public class GithubTestProperties {
    private String owner;
    private String repo;
    private String token;

    @ConstructorBinding
    public GithubTestProperties(String owner, String repo, String token) {
        this.owner = owner;
        this.repo = repo;
        this.token = token;
        log.info("Initializing GithubTestProperties with owner: {}, repo: {}, token: {}", owner, repo, token);
    }

}
