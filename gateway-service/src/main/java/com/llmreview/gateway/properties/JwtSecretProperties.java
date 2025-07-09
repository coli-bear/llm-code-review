package com.llmreview.gateway.properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Slf4j
@Getter
@ConfigurationProperties(prefix = "com.llmreview.jwt")
public class JwtSecretProperties {

    private final String secretKey;
    private final String issuer;

    @ConstructorBinding
    public JwtSecretProperties(String secretKey, String issuer) {
        log.debug("Construct JwtSecretProperties");
        log.debug("secretKey: {}", secretKey);
        log.debug("issuer: {}", issuer);
        this.secretKey = secretKey;
        this.issuer = issuer;
    }

}
