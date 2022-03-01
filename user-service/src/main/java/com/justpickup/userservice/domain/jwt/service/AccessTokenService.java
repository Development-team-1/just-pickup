package com.justpickup.userservice.domain.jwt.service;

public interface AccessTokenService {

    void checkAccessToken(String authorizationHeader);
}
