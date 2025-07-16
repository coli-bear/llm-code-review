package com.llmreview.api.scm.github.client;

import lombok.ToString;

public record GithubRepoInfoRequest(String owner, String repo, String token) {
    public GithubRepoInfoRequest {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Owner must not be null or blank");
        }
        if (repo == null || repo.isBlank()) {
            throw new IllegalArgumentException("Repository name must not be null or blank");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token must not be null or blank");
        }
    }
}
