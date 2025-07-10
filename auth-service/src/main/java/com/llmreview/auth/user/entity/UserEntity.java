package com.llmreview.auth.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access =  AccessLevel.PROTECTED)
@Getter
@Builder
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

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum PlanType {
        FREE, STANDARD, PRO
    }

    public enum Role {
        USER, ADMIN
    }
}
