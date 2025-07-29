package com.llmreview.api.scm.github.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GithubProjectReadDto extends GithubProjectDto {
    private final Long projectId;

    @Builder
    protected GithubProjectReadDto(String projectName, String projectRepoName, String projectRepoOwner, String projectUrl, String projectDescription, Long projectId) {
        super(projectName, projectRepoName, projectRepoOwner, projectUrl, projectDescription);
        this.projectId = projectId;
    }
}
