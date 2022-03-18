package com.justpickup.userservice.domain.user.dto;

import com.justpickup.userservice.domain.user.entity.StoreOwner;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreOwnerDto extends UserDto {
    private String businessNumber;

    @Builder
    public StoreOwnerDto(Long id, String email, String password, String name,
                         String phoneNumber, String dtype, String businessNumber) {
        super(id, email, password, name, phoneNumber, dtype);
        this.businessNumber = businessNumber;
    }

    public static StoreOwnerDto of(StoreOwner storeOwner) {
        return new StoreOwnerDto(storeOwner.getId(), storeOwner.getEmail(), storeOwner.getPassword(),
                storeOwner.getName(), storeOwner.getPhoneNumber(), storeOwner.getDtype(), storeOwner.getBusinessNumber());
    }
}
