package com.llmreview.api.scm.github.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GithubProjectCreateDto extends GithubProjectDto {
    private final String githubToken;
    private final LocalDateTime tokenExpireAt;

    @Builder
    protected GithubProjectCreateDto(String projectName, String projectRepoName, String projectRepoOwner, String projectUrl, String projectDescription, String githubToken, LocalDateTime tokenExpireAt) {
        super(projectName, projectRepoName, projectRepoOwner, projectUrl, projectDescription);
        this.githubToken = githubToken;
        this.tokenExpireAt = tokenExpireAt;
    }
}
