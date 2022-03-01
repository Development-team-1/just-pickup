package com.justpickup.userservice.domain.jwt.service;

import com.justpickup.userservice.domain.jwt.exception.AccessTokenNotValidException;
import com.justpickup.userservice.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

    private final JwtTokenProvider tokenProvider;

    @Override
    public void checkAccessToken(String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer", "");

        if (!tokenProvider.validateJwtToken(token)) {
            throw new AccessTokenNotValidException("Access Token is not Valid");
        }
    }
}
