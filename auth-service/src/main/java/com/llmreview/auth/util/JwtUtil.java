package com.llmreview.auth.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.llmreview.auth.properties.JwtSecretProperties;
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

    public String generateToken(String githubId) {
        return JWT.create()
            .withSubject(githubId)
            .withIssuedAt(new Date())
            .withIssuer(jwtSecretProperties.getIssuer())
            .withExpiresAt(new Date(System.currentTimeMillis() + jwtSecretProperties.getExpirationTime()))
            .withAudience(jwtSecretProperties.getAudience())
            .sign(algorithm);
    }

    public String validateAndGetSubject(String token) {
        return JWT.require(algorithm)
            .build()
            .verify(token)
            .getSubject();
    }
}

