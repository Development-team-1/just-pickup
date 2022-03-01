package com.justpickup.userservice.domain.user.service;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;

public interface UserService {
    CustomerDto findCustomerByUserId(Long userId);
    void saveStoreOwner(StoreOwnerDto storeOwnerDto);
}
