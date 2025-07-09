package com.llmreview.auth.user.repoaitory;

import com.llmreview.auth.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByGithubId(String githubId);
}
