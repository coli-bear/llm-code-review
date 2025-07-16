package com.llmreview.api.scm.github.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "scm_github_project")
public class GithubProject {
    @Id
    @Column(name ="project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(name = "project_name", nullable = false, length = 64)
    private String projectName;

    @Column(name = "project_url", nullable = false, length = 512)
    private String projectUrl;

    @Column(name = "project_repo_name", nullable = false, length = 128)
    private String projectRepoName;
    @Column(name = "project_repo_owner", nullable = false, length = 64)
    private String projectRepoOwner;

    @Embedded
    private GithubToken githubToken;
}
