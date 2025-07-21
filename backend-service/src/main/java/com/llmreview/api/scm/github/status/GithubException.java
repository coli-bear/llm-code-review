package com.llmreview.api.scm.github.status;

import lombok.Getter;

@Getter
public abstract class GithubException extends RuntimeException {
    protected GithubException(String message) {
        super(message);
    }
}
