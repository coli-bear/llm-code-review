package com.llmreview.auth.user.service;

import com.llmreview.auth.user.entity.UserEntity;
import com.llmreview.auth.user.repoaitory.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String githubId = String.valueOf(attributes.get("id"));
        String username = String.valueOf(attributes.get("login"));
        String email = String.valueOf(attributes.get("email"));

        UserEntity userEntity = userRepository.findByGithubId(githubId)
            .orElseGet(() -> userRepository.save(
                UserEntity.builder()
                    .githubId(githubId)
                    .username(username)
                    .email(email)
                    .plan(UserEntity.PlanType.FREE) // Default plan type
                    .role(UserEntity.Role.USER)
                    .build()
            ));

        log.debug("User details: id={}, username={}, email={}, role={}", userEntity.getGithubId(), userEntity.getUsername(), userEntity.getEmail(), userEntity.getRole());

        return oAuth2User;
    }
}
