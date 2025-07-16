package com.llmreview.api.scm.github.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
public class GithubToken {
    @Column(name = "token", nullable = false, length = 512)
    private String token;
    private LocalDateTime createAt;
}
