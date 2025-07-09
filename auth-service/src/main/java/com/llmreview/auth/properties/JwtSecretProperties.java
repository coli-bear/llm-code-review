package com.llmreview.auth.properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Slf4j
@Getter
@ConfigurationProperties(prefix = "com.llmreview.jwt")
public class JwtSecretProperties {

    private final String secretKey;
    private final String issuer;
    private final String audience;
    private final long expirationTime; // Default to 1 hour

    @ConstructorBinding
    public JwtSecretProperties(String secretKey, String issuer, String audience, long expirationTime) {
        log.debug("Construct JwtSecretProperties");
        log.debug("secretKey: {}", secretKey);
        log.debug("issuer: {}", issuer);
        log.debug("audience: {}", audience);
        log.debug("expirationTime: {}", expirationTime);
        this.secretKey = secretKey;
        this.issuer = issuer;
        this.audience = audience;
        this.expirationTime = expirationTime;
    }

}
