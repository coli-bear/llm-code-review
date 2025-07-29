package com.llmreview.api.scm.github.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access =  AccessLevel.PRIVATE)
@Builder
@Table(
    name = "scm_github_project",
    indexes = {
        @Index(name = "idx_project_name", columnList = "project_name"),
        @Index(name = "idx_project_repo_name", columnList = "project_repo_name"),
    }
)
public class GithubProject {
    @Id
    @Column(name ="project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Embedded
    private GithubProjectMeta githubProjectMeta;

    @Column(name = "project_url", nullable = false, length = 512)
    private String projectUrl;

    @Column(name = "project_repo_name", nullable = false, length = 128)
    private String projectRepoName;

    @Embedded
    private GithubToken githubToken;

    @Column(name = "project_description", length = 512)
    private String projectDescription;
}
