package com.justpickup.userservice.domain.user.service;

import com.justpickup.userservice.domain.user.dto.CustomerDto;

public interface UserService {
    CustomerDto findCustomerByUserId(Long userId);

    String authByGithub(String code);
}
