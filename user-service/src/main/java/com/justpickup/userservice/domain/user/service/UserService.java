package com.justpickup.userservice.domain.user.service;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;
import com.justpickup.userservice.domain.user.entity.StoreOwner;

public interface UserService {


    CustomerDto findCustomerByUserId(Long userId);
    StoreOwner saveStoreOwner(StoreOwnerDto storeOwnerDto);

}
