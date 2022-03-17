package com.justpickup.ownerapigatewayservice.filter;

import com.justpickup.ownerapigatewayservice.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthorizationHeaderFilter(JwtTokenProvider jwtTokenProvider) {
        super(Config.class);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("AuthorizationHeaderFilter Start: request -> {}", exchange.getRequest());

            HttpHeaders headers = request.getHeaders();
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = headers.get(HttpHeaders.AUTHORIZATION).get(0);

            // JWT 토큰 판별
            String token = authorizationHeader.replace("Bearer", "");

            jwtTokenProvider.validateJwtToken(token);

            String subject = jwtTokenProvider.getUserId(token);

            if (false == jwtTokenProvider.getRoles(token).contains("StoreOwner")) {
                return onError(exchange, "권한 없음", HttpStatus.UNAUTHORIZED);
            }

            ServerHttpRequest newRequest = request.mutate()
                    .header("user-id", subject)
                    .build();

            log.info("AuthorizationHeaderFilter End");
            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }

    // Mono(단일 값), Flux(다중 값) -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String errorMsg, HttpStatus httpStatus) {
        log.error(errorMsg);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }
}
