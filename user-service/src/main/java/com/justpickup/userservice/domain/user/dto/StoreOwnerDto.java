package com.justpickup.userservice.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreOwnerDto extends UserDto {
    private String businessNumber;

    @Builder
    public StoreOwnerDto(Long id, String email, String password, String name,
                         String phoneNumber, String dtype, String businessNumber, String refreshTokenId) {
        super(id, email, password, name, phoneNumber, dtype, refreshTokenId);
        this.businessNumber = businessNumber;
    }
}
