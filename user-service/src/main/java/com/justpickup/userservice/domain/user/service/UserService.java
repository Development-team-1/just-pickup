package com.justpickup.userservice.domain.user.service;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;
import com.justpickup.userservice.domain.user.entity.StoreOwner;

public interface UserService extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    void updateRefreshToken(Long id, String refreshToken);
    CustomerDto findCustomerByUserId(Long userId);
    StoreOwner saveStoreOwner(StoreOwnerDto storeOwnerDto);
}
