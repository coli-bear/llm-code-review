package com.llmreview.gateway.filter;

import com.llmreview.gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * JwtAuthFilter는 Spring Cloud Gateway에서 JWT 인증을 처리하는 필터입니다.
 * 이 필터는 요청 헤더에서 JWT 토큰을 추출하고, 유효성을 검사하여
 * 인증된 사용자만이 요청을 처리할 수 있도록 합니다.
 */
@Slf4j
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * JWT 인증 필터로, 요청 헤더에서 JWT 토큰을 추출하고 검증합니다.
     * 토큰이 없거나 유효하지 않은 경우, 적절한 HTTP 상태 코드를 반환합니다.
     * @param exchange - ServerWebExchange 객체로 요청과 응답을 처리합니다.
     * @param chain - GatewayFilterChain 객체로 다음 필터를 호출합니다.
     * @return Mono<Void> - 비동기 처리를 위한 Mono 객체를 반환합니다.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("JwtAuthFilter is processing the request: {}", exchange.getRequest().getPath());
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
        log.debug("Authorization header: {}", authorizationHeader);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            log.debug("Authorization header is missing or does not start with Bearer prefix");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String token = authorizationHeader.substring(BEARER_PREFIX.length());
        log.debug("Extracted token: {}", token);
        boolean validated = jwtUtil.validateToken(token);
        log.debug("Token validation result: {}", validated);
        if (!validated) {
            log.debug("Invalid JWT token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    /**
     * 우선순위를 설정하기위한 메서드로 Ordered 인터페이스를 구현합니다.
     * 이 필터는 다른 필터보다 먼저 실행되어야 하므로 낮은 숫자를 반환합니다.
     * @see org.springframework.core.Ordered
     * @return -1
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
