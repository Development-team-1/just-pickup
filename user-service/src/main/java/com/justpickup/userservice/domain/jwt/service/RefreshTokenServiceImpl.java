package com.justpickup.userservice.domain.jwt.service;

import com.justpickup.userservice.domain.jwt.exception.TokenRefreshException;
import com.justpickup.userservice.domain.user.dto.JwtTokenDto;
import com.justpickup.userservice.domain.user.entity.User;
import com.justpickup.userservice.domain.user.exception.NotExistUserException;
import com.justpickup.userservice.domain.user.repository.UserRepository;
import com.justpickup.userservice.domain.jwt.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;



    @Transactional
    @Override
    public JwtTokenDto refreshJwtToken(String accessToken, String refreshToken) {
        String userId = jwtTokenProvider.getUserId(accessToken);

        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new NotExistUserException("사용자 고유번호 : " + userId + "는 없는 사용자입니다."));

        // refresh token 검증
        if (!jwtTokenProvider.validateJwtToken(refreshToken)) {
            // 익셉션 발생 - 로그 아웃 후 로그인 페이지로 이동 처리
            user.deleteRefreshToken();
            throw new TokenRefreshException("Not validate jwt token = " + refreshToken);
        }

        String userRefreshTokenId = user.getRefreshTokenId();
        if (!jwtTokenProvider.equalRefreshTokenId(userRefreshTokenId, refreshToken)) {
            // 익셉션 발생 - 로그인 아웃 후 로그인 페이지로 이동 처리
            user.deleteRefreshToken();
            throw new TokenRefreshException("Not equal jwt token! user = " + userRefreshTokenId +
                    ", refreshToken = " + refreshToken);
        }

        Authentication authentication = getAuthentication(user.getEmail());
        List<String> roles = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String newAccessToken = jwtTokenProvider.createJwtAccessToken(userId, "/refreshToken", roles);
        String newRefreshToken = jwtTokenProvider.createJwtRefreshToken();

        user.changeRefreshToken(newRefreshToken);

        return JwtTokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public Authentication getAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

}
