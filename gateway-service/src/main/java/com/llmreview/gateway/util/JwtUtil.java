package com.llmreview.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.llmreview.gateway.properties.JwtSecretProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    private final Algorithm algorithm;

    public JwtUtil(JwtSecretProperties jwtSecretProperties) {
        this.algorithm = Algorithm.HMAC512(jwtSecretProperties.getSecretKey());
    }

    public boolean validateToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                log.debug("Token is null or empty");
                return false;
            }
            log.debug("Validating token: {}", token);
            JWT.require(algorithm)
                .build()
                .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String getSubject(String token) {
        return JWT.require(algorithm)
            .build()
            .verify(token)
            .getSubject();
    }


    public String extractRole(String token) {
        DecodedJWT decodedJWT = JWT.require(algorithm)
            .build()
            .verify(token);
        return decodedJWT.getClaim(ClaimKey.ROLE.getKey()).asString();
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