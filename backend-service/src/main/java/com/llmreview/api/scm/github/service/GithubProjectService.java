package com.llmreview.api.scm.github.service;

import com.llmreview.api.scm.github.client.GithubClientRequestInfo;
import com.llmreview.api.scm.github.client.GithubRepoMetaClient;
import com.llmreview.api.scm.github.entity.GithubProject;
import com.llmreview.api.scm.github.entity.GithubProjectMeta;
import com.llmreview.api.scm.github.entity.GithubToken;
import com.llmreview.api.scm.github.repository.GithubProjectRepository;
import com.llmreview.api.scm.github.service.dto.GithubProjectCreateDto;
import com.llmreview.api.scm.github.service.dto.GithubProjectReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubProjectService {
    private final GithubProjectRepository repository;
    private final GithubRepoMetaClient githubRepoMetaClient;

    /**
     * Github 프로젝트 생성
     *
     * @param projectCreateDto Github 프로젝트 생성 요청 DTO
     * @return GithubProjectReadDto
     * @see GithubProjectCreateDto
     */
    public GithubProjectReadDto createProject(GithubProjectCreateDto projectCreateDto) {
        GithubProjectMeta projectMeta = GithubProjectMeta.builder()
            .projectName(projectCreateDto.getProjectName())
            .projectRepoOwner(projectCreateDto.getProjectRepoOwner())
            .build();
        boolean existsProjectMeta = this.repository.existsByGithubProjectMeta(projectMeta);
        if (existsProjectMeta) {
            // TODO: 예외처리 개선
            throw new RuntimeException(String.format("Already exists repo: %s/'%s'", projectCreateDto.getProjectRepoOwner(), projectCreateDto.getProjectRepoName()));
        }

        GithubToken githubToken = GithubToken.builder()
            .token(projectCreateDto.getGithubToken())
            .expiresAt(projectCreateDto.getTokenExpireAt())
            .build();

        GithubProject githubProject = GithubProject.builder()
            .githubProjectMeta(projectMeta)
            .projectDescription(projectCreateDto.getProjectDescription())
            .projectRepoName(projectCreateDto.getProjectRepoName())
            .projectUrl(projectCreateDto.getProjectUrl())
            .githubToken(githubToken)
            .build();

        GithubClientRequestInfo githubClientRequestInfo = new GithubClientRequestInfo(projectCreateDto.getProjectRepoOwner(), projectCreateDto.getProjectRepoName(), projectCreateDto.getGithubToken());
        githubRepoMetaClient.request(githubClientRequestInfo);

        GithubProject savedProject = this.repository.save(githubProject);
        return GithubProjectReadDto.builder()
            .projectId(savedProject.getProjectId())
            .projectName(savedProject.getGithubProjectMeta().getProjectName())
            .projectRepoName(savedProject.getProjectRepoName())
            .projectRepoOwner(savedProject.getGithubProjectMeta().getProjectRepoOwner())
            .projectUrl(savedProject.getProjectUrl())
            .projectDescription(savedProject.getProjectDescription())
            .build();
    }
}
