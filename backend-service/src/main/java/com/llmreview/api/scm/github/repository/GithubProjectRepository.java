package com.llmreview.api.scm.github.repository;

import com.llmreview.api.scm.github.entity.GithubProject;
import com.llmreview.api.scm.github.entity.GithubProjectMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubProjectRepository extends JpaRepository<GithubProject, Long> {
    boolean existsByGithubProjectMeta(GithubProjectMeta projectMeta);
}
