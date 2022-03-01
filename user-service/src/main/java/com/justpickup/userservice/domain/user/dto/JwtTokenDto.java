package com.justpickup.userservice.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class JwtTokenDto {
    private String accessToken;
    private Date accessTokenExpiredDate;
    private String refreshToken;

    @Builder
    public JwtTokenDto(String accessToken, String refreshToken, Date accessTokenExpiredDate) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiredDate = accessTokenExpiredDate;
    }
}
