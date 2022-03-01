package com.justpickup.userservice.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.userservice.domain.jwt.service.RefreshTokenServiceImpl;
import com.justpickup.userservice.global.utils.JwtTokenProvider;
import com.justpickup.userservice.global.dto.LoginRequest;
import com.justpickup.userservice.global.dto.Result;
import com.justpickup.userservice.global.utils.CookieProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final CookieProvider cookieProvider;

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

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String userId = user.getUsername();

        String accessToken = jwtTokenProvider.createJwtAccessToken(userId, request.getRequestURI(), roles);
        Date expiredTime = jwtTokenProvider.getExpiredTime(accessToken);
        String refreshToken = jwtTokenProvider.createJwtRefreshToken();

        refreshTokenServiceImpl.updateRefreshToken(Long.valueOf(userId), jwtTokenProvider.getRefreshTokenId(refreshToken));

        // 쿠키 설정
        ResponseCookie refreshTokenCookie = cookieProvider.createRefreshTokenCookie(refreshToken);

        Cookie cookie = cookieProvider.of(refreshTokenCookie);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.addCookie(cookie);

        // body 설정
        Map<String, Object> tokens = Map.of(
                "accessToken", accessToken,
                "expiredTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime)
        );

        new ObjectMapper().writeValue(response.getOutputStream(), Result.createSuccessResult(tokens));
    }
}
