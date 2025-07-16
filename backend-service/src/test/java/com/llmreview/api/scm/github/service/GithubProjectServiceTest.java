package com.llmreview.api.scm.github.service;

import com.llmreview.api.scm.github.repository.GithubProjectRepository;
import com.llmreview.api.scm.github.service.dto.GithubProjectCreateDto;
import com.llmreview.api.scm.github.service.dto.GithubProjectReadDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active=test")
@TestPropertySource(locations = "classpath:application-test.yaml")
class GithubProjectServiceTest {

    @Autowired
    GithubProjectService githubProjectService;

    @Autowired
    GithubProjectRepository repository;

    /**
     * GithubProjectService.createProject() 메서드가 정상적으로 프로젝트를 생성하는지 테스트
     * - 프로젝트 생성 DTO를 입력으로 받아, 생성된 프로젝트 DTO를 반환하는지 확인
     */
    @Test
    @DisplayName("Github 프로젝트 생성 테스트")
    void createProject() {
        GithubProjectCreateDto createDto1 = GithubProjectCreateDto.builder()
            .projectName("projectName1")
            .projectDescription("projectDescription1")
            .projectRepoName("projectRepoName1")
            .projectRepoOwner("projectRepoOwner1")
            .projectUrl("https://github.com/1")
            .githubToken("token1")
            .expiresAt(LocalDateTime.MIN)
            .build();
        GithubProjectReadDto project1 = this.githubProjectService.createProject(createDto1);

        assertThat(project1).isNotNull();
        assertThat(project1.getProjectName()).isEqualTo(createDto1.getProjectName());
        assertThat(project1.getProjectDescription()).isEqualTo(createDto1.getProjectDescription());
        assertThat(project1.getProjectRepoName()).isEqualTo(createDto1.getProjectRepoName());
        assertThat(project1.getProjectRepoOwner()).isEqualTo(createDto1.getProjectRepoOwner());
        assertThat(project1.getProjectUrl()).isEqualTo(createDto1.getProjectUrl());
        assertThat(project1.getProjectId()).isNotNull();
        assertThat(project1.getProjectId()).isOne();

        assertThat(repository.count()).isOne();

        GithubProjectCreateDto createDto2 = GithubProjectCreateDto.builder()
            .projectName("projectName2")
            .projectDescription("projectDescription2")
            .projectRepoName("projectRepoName2")
            .projectRepoOwner("projectRepoOwner2")
            .projectUrl("https://github.com/1")
            .githubToken("token2")
            .expiresAt(LocalDateTime.MIN)
            .build();

        GithubProjectReadDto project2 = this.githubProjectService.createProject(createDto2);
        assertThat(project2).isNotNull();
        assertThat(project2.getProjectName()).isEqualTo(createDto2.getProjectName());
        assertThat(project2.getProjectDescription()).isEqualTo(createDto2.getProjectDescription());
        assertThat(project2.getProjectRepoName()).isEqualTo(createDto2.getProjectRepoName());
        assertThat(project2.getProjectRepoOwner()).isEqualTo(createDto2.getProjectRepoOwner());
        assertThat(project2.getProjectUrl()).isEqualTo(createDto2.getProjectUrl());
        assertThat(project2.getProjectId()).isNotNull();
        assertThat(project2.getProjectId()).isEqualTo(2L);
        assertThat(repository.count()).isEqualTo(2L);

    }

    /**
     *
     */
}