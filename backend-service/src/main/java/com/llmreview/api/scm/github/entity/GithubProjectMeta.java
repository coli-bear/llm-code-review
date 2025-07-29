package com.llmreview.api.scm.github.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class GithubProjectMeta {
    @Column(name = "project_name", nullable = false, length = 64)
    private String projectName;

    @Column(name = "project_repo_owner", nullable = false, length = 64)
    private String projectRepoOwner;
}
