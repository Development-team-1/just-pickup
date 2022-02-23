package com.justpickup.userservice.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtTokenDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    public JwtTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
