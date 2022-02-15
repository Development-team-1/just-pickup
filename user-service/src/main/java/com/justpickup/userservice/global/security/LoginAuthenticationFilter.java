package com.justpickup.userservice.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.userservice.global.dto.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // login 리퀘스트 패스로 오는 요청을 판단
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication authentication;

        try {
            LoginRequest credential = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword())
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return authentication;
    }

    // 로그인 성공 이후 토큰 생성
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        org.springframework.security.core.userdetails.User user = (User) authResult.getPrincipal();

        String accessToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 10 * 60 * 1000)
                )
                .signWith(SignatureAlgorithm.HS512, "your-256-bit-secret")
                .setIssuer(request.getRequestURI())
                .addClaims(Map.of("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 30 * 60 * 1000)
                )
                .signWith(SignatureAlgorithm.HS512, "your-256-bit-secret")
                .setIssuer(request.getRequestURI())
                .compact();

        Map<String, String> tokens = Map.of(
                "access_token", accessToken,
                "refresh_token", refreshToken
        );
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication
            (HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        log.warn("로그인 실패!!");
    }
}
