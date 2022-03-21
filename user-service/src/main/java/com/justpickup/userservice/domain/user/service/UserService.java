package com.justpickup.userservice.domain.user.service;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.dto.PostOwnerDto;
import com.justpickup.userservice.domain.user.dto.PostStoreDto;
import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;

import java.util.List;

public interface UserService {
    CustomerDto findCustomerByUserId(Long userId);
    List<CustomerDto> findCustomerByUserIds(List<Long> userIds);
    StoreOwnerDto findOwnerById(Long userId);
    void saveStoreOwner(PostOwnerDto toPostOwnerDto, PostStoreDto toPostStoreDto);
}
