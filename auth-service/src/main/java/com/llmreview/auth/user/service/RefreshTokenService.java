package com.llmreview.auth.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final Duration REFRESH_TOKEN_EXPIRE = Duration.ofHours(2);

    public void saveRefreshToken(String githubId, String refreshToken) {
        String key = getKey(githubId);
        redisTemplate.opsForValue().set(key, refreshToken, REFRESH_TOKEN_EXPIRE);
    }

    public Optional<String> getRefreshToken(String githubId) {
        String key = getKey(githubId);
        String token = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(token);
    }

    public String getKey(String githubId) {
        return "refresh_token:" + githubId;
    }
}
