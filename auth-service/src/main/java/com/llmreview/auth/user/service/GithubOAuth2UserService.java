package com.llmreview.auth.user.service;

import com.llmreview.auth.user.entity.UserEntity;
import com.llmreview.auth.user.repoaitory.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public GithubOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String githubId = String.valueOf(attributes.get("id"));
        String username = String.valueOf(attributes.get("login"));
        String email = String.valueOf(attributes.get("email"));

        userRepository.findByGithubId(githubId)
            .orElseGet(() -> userRepository.save(
                new UserEntity(githubId, username, email, UserEntity.PlanType.FREE)
            ));

        return oAuth2User;
    }
}
