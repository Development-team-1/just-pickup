package com.justpickup.ownerapigatewayservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        List<Class<? extends RuntimeException>> jwtExceptions =
                List.of(SignatureException.class,
                        MalformedJwtException.class,
                        UnsupportedJwtException.class,
                        IllegalArgumentException.class);
        Class<? extends Throwable> exceptionClass = ex.getClass();

        Map<String, Object> responseBody = new HashMap<>();
        if (exceptionClass == ExpiredJwtException.class) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            responseBody.put("code", "EXPIRED");
            responseBody.put("message", "Access Token is Expired!");
        } else if (jwtExceptions.contains(exceptionClass)){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            responseBody.put("code", "INVALID");
            responseBody.put("message", "Invalid Access Token");
        } else {
            exchange.getResponse().setStatusCode(exchange.getResponse().getStatusCode());
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            responseBody.put("code", ex.getMessage());
        }

        DataBuffer wrap = null;
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(responseBody);
            wrap = exchange.getResponse().bufferFactory().wrap(bytes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return exchange.getResponse().writeWith(Flux.just(wrap));
    }

}
