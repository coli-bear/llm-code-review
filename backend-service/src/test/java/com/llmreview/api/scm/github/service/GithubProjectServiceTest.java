package com.llmreview.api.scm.github.service;

import com.llmreview.api.scm.github.repository.GithubProjectRepository;
import com.llmreview.api.scm.github.service.dto.GithubProjectCreateDto;
import com.llmreview.api.scm.github.service.dto.GithubProjectReadDto;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@TestPropertySource(locations = "classpath:application-test.yaml")
class GithubProjectServiceTest {

    @Autowired
    GithubProjectService githubProjectService;

    @Autowired
    GithubProjectRepository repository;

    public static Stream<Arguments> createProjectDtoProvider() {
        Dotenv dotenv = Dotenv.configure().filename(".env.test").load();

        String githubOwner = dotenv.get("GITHUB_OWNER");
        String githubPublicRepo = dotenv.get("GITHUB_PUBLIC_REPO");
        String githubToken = dotenv.get("GITHUB_PUBLIC_REPO_TOKEN");
        GithubProjectCreateDto create1 = generateProjectCreateDto("projectName1", "projectDescription1",
            githubPublicRepo, githubOwner, "https://github.com/1", githubToken, LocalDateTime.MIN);

        GithubProjectCreateDto create2 = generateProjectCreateDto("projectName2", "projectDescription2",
            githubPublicRepo, githubOwner, "https://github.com/1", githubToken, LocalDateTime.MIN);

        return Stream.of(Arguments.of(create1, 1L), Arguments.of(create2, 2L));
    }

    private static GithubProjectCreateDto generateProjectCreateDto(
        String projectName, String projectDescription, String projectRepoName, String projectRepoOwner, String projectUrl,
        String githubToken, LocalDateTime githubTokenExpireAt
    ) {
        return GithubProjectCreateDto.builder()
            .projectName(projectName)
            .projectDescription(projectDescription)
            .projectRepoName(projectRepoName)
            .projectRepoOwner(projectRepoOwner)
            .projectUrl(projectUrl)
            .githubToken(githubToken)
            .tokenExpireAt(githubTokenExpireAt)
            .build();
    }

    /**
     * GithubProjectService.createProject() 메서드가 정상적으로 프로젝트를 생성하는지 테스트
     * - 프로젝트 생성 DTO를 입력으로 받아, 생성된 프로젝트 DTO를 반환하는지 확인
     */
    @Order(0)
    @ParameterizedTest
    @MethodSource("createProjectDtoProvider")
    @DisplayName("Github 프로젝트 생성 테스트")
    void createProject(GithubProjectCreateDto projectCreateDto, Long expectedProjectId) {
        GithubProjectReadDto createdProject = this.githubProjectService.createProject(projectCreateDto);

        assertThat(createdProject).isNotNull();
        assertThat(createdProject.getProjectName()).isEqualTo(projectCreateDto.getProjectName());
        assertThat(createdProject.getProjectDescription()).isEqualTo(projectCreateDto.getProjectDescription());
        assertThat(createdProject.getProjectRepoName()).isEqualTo(projectCreateDto.getProjectRepoName());
        assertThat(createdProject.getProjectRepoOwner()).isEqualTo(projectCreateDto.getProjectRepoOwner());
        assertThat(createdProject.getProjectUrl()).isEqualTo(projectCreateDto.getProjectUrl());
        assertThat(createdProject.getProjectId()).isNotNull();
        assertThat(createdProject.getProjectId()).isEqualTo(expectedProjectId);
        assertThat(repository.count()).isEqualTo(expectedProjectId);
    }

    @Test
    @DisplayName("Github 프로젝트 생성 - 동일한 소유자가 동일한 이름의 프로젝트가 이미 존재하는 경우 예외 발생")
    void createProjectWithDuplicateName() {
        Dotenv dotenv = Dotenv.configure().filename(".env.test").load();
        String duplicateProjectName = "duplicateProject";

        String githubOwner = dotenv.get("GITHUB_OWNER");
        String githubPublicRepo = dotenv.get("GITHUB_PUBLIC_REPO");
        String githubToken = dotenv.get("GITHUB_PUBLIC_REPO_TOKEN");
        String githubUrl = "https://github.com/duplicateProject";

        GithubProjectCreateDto projectCreateDto = generateProjectCreateDto(
            duplicateProjectName, "description", githubPublicRepo, githubOwner, githubUrl,
            githubToken, LocalDateTime.MIN);

        this.githubProjectService.createProject(projectCreateDto);

        // 동일한 이름의 프로젝트 생성 시도
        GithubProjectCreateDto duplicateProjectCreateDto = generateProjectCreateDto(
            duplicateProjectName, "description", githubPublicRepo, githubOwner, githubUrl,
            githubToken, LocalDateTime.MIN);
        assertThatThrownBy(() -> this.githubProjectService.createProject(duplicateProjectCreateDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(String.format("Already exists repo: %s/'%s'", projectCreateDto.getProjectRepoOwner(), projectCreateDto.getProjectRepoName()));
    }
}