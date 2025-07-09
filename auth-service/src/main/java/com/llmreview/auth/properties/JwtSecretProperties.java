package com.llmreview.auth.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "com.llmreview.jwt")
public class JwtSecretProperties {

    private static final Logger logger = LoggerFactory.getLogger(JwtSecretProperties.class);

    private final String secretKey;
    private final String issuer;
    private final String audience;
    private final long expirationTime; // Default to 1 hour

    @ConstructorBinding
    public JwtSecretProperties(String secretKey, String issuer, String audience, long expirationTime) {
        logger.debug("Construct JwtSecretProperties");
        logger.debug("secretKey: {}", secretKey);
        logger.debug("issuer: {}", issuer);
        logger.debug("audience: {}", audience);
        logger.debug("expirationTime: {}", expirationTime);
        this.secretKey = secretKey;
        this.issuer = issuer;
        this.audience = audience;
        this.expirationTime = expirationTime;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAudience() {
        return audience;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
