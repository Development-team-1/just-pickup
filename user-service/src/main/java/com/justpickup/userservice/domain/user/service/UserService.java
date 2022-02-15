package com.justpickup.userservice.domain.user.service;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    CustomerDto findCustomerByUserId(Long userId);

}
