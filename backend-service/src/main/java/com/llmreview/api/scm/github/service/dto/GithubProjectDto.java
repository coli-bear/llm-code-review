package com.llmreview.api.scm.github.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class GithubProjectDto {
    private final String projectName;
    private final String projectRepoName;
    private final String projectRepoOwner;
    private final String projectUrl;
    private final String projectDescription;
}
