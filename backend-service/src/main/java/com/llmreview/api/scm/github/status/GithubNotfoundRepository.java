package com.llmreview.api.scm.github.status;

public class GithubNotfoundRepository extends GithubException {
    public GithubNotfoundRepository(String owner, String repo) {
        super(String.format("Repository not found: %s/%s", owner, repo));
    }
}
