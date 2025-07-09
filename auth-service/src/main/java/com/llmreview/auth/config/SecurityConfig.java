package com.llmreview.auth.config;

import com.llmreview.auth.controller.GithubOAuth2LoginSuccessHandler;
import com.llmreview.auth.user.service.GithubOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GithubOAuth2UserService githubOAuth2UserService;
    private final GithubOAuth2LoginSuccessHandler githubOAuth2LoginSuccessHandler;

    public SecurityConfig(GithubOAuth2UserService githubOAuth2UserService, GithubOAuth2LoginSuccessHandler githubOAuth2LoginSuccessHandler) {
        this.githubOAuth2UserService = githubOAuth2UserService;
        this.githubOAuth2LoginSuccessHandler = githubOAuth2LoginSuccessHandler;
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .oauth2Login(oauth2 ->
                oauth2
                    .userInfoEndpoint(userInfo ->
                        userInfo.userService(githubOAuth2UserService)
                    ).successHandler(githubOAuth2LoginSuccessHandler)

            );
        return http.build();
    }
}
