package com.justpickup.userservice.domain.jwt.service;

import com.justpickup.userservice.domain.user.dto.JwtTokenDto;

public interface RefreshTokenService {
    void updateRefreshToken(Long id, String uuid);
    JwtTokenDto refreshJwtToken(String accessToken, String refreshToken);
    void logoutToken(String accessToken);
}
