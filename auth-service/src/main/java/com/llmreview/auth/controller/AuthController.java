package com.llmreview.auth.controller;

import com.llmreview.auth.user.service.RefreshTokenService;
import com.llmreview.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private static final String ERROR_KEY = "error";

    @PostMapping("/reissue")
    public ResponseEntity<Map<String, String>> reissueToken(@RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(ERROR_KEY, "Refresh token is missing or invalid"));
        }

        String accessTokenValue = accessToken.substring(7);
        String githubId = jwtUtil.getSubject(accessTokenValue);
        if (githubId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(ERROR_KEY, "Invalid access token"));
        }
        Optional<String> optional = refreshTokenService.getRefreshToken(githubId);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(ERROR_KEY, "Expired session"));
        }
        String refreshToken = optional.get();
        String subject = jwtUtil.validateAndGetSubject(refreshToken);
        if (subject == null || !subject.equals(githubId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(ERROR_KEY, "Invalid refresh token"));
        }
        String newToken = jwtUtil.generateToken(githubId, "USER");
        return ResponseEntity.ok(Map.of("accessToken", newToken, "tokenType", "Bearer"));
    }
}
