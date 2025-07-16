package com.llmreview.api.scm.github.status;

public class GithubNotfoundRepository extends RuntimeException{
    public GithubNotfoundRepository(String owner, String repo) {
        super(String.format("Repository %s/%s not found", owner, repo));
    }
}
