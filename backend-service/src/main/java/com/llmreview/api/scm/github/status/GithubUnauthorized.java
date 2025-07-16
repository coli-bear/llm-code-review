package com.llmreview.api.scm.github.status;

public class GithubUnauthorized extends RuntimeException {
    public GithubUnauthorized(String owner, String repo) {
        super(String.format("Unauthorized access to repository: %s/%s", owner, repo));
    }
}
