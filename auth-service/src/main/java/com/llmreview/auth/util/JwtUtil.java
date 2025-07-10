package com.llmreview.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.llmreview.auth.properties.JwtSecretProperties;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final JwtSecretProperties jwtSecretProperties;
    private final Algorithm algorithm;

    public JwtUtil(JwtSecretProperties jwtSecretProperties) {
        this.jwtSecretProperties = jwtSecretProperties;
        this.algorithm = Algorithm.HMAC512(jwtSecretProperties.getSecretKey());
    }

    public String generateToken(String githubId, String role) {
        return JWT.create()
            .withSubject(githubId)
            .withIssuedAt(new Date())
            .withIssuer(jwtSecretProperties.getIssuer())
            .withExpiresAt(new Date(System.currentTimeMillis() + jwtSecretProperties.getExpirationTime()))
            .withAudience(jwtSecretProperties.getAudience())
            .withClaim(ClaimKey.ROLE.getKey(), role)
            .sign(algorithm);
    }

    public String validateAndGetSubject(String token) {
        return JWT.require(algorithm)
            .build()
            .verify(token)
            .getSubject();
    }

    @Getter
    public enum ClaimKey {
        ROLE("role");

        private final String key;

        ClaimKey(String key) {
            this.key = key;
        }
    }
}

