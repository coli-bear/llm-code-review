package com.llmreview.auth.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String githubId;
    private String username;
    private String email;

    @Enumerated(EnumType.STRING)
    private PlanType plan;

    protected UserEntity() {
    }

    public UserEntity(String githubId, String username, String email, PlanType plan) {
        this.githubId = githubId;
        this.username = username;
        this.email = email;
        this.plan = plan;
    }

    public enum PlanType {
        FREE, STANDARD, PRO
    }
}
