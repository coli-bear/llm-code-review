package com.llmreview.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            // GlobalFilter 전에 Spring Security 의 AutorizationWebFilter 가 먼저 트리거 되어 401을 반환
            // 따라서 아래 .authorizeExchange() 설정은 필요하지 않음
            .authorizeExchange(ex -> ex
                .anyExchange().permitAll()
            )
            .build();
    }
}
