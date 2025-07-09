package com.llmreview.auth.controller;

import com.llmreview.auth.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Component
public class GithubOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private static final String FRONTEND_REDIRECT_URL = "http://localhost:3000/oauth-success?token=";
    private static final String ERROR_REDIRECT_URL = "http://localhost:3000/oauth-error";

    public GithubOAuth2LoginSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Object ids = oAuth2User.getAttribute("id");
        if (ids == null) {
            String errorUrl = URLEncoder.encode(ERROR_REDIRECT_URL + "?reason=missing github id", StandardCharsets.UTF_8);
            response.sendRedirect(errorUrl);
        }
        String githubId = String.valueOf(ids);
        String jwt = jwtUtil.generateToken(githubId);

        response.sendRedirect(FRONTEND_REDIRECT_URL + jwt);
    }
}
