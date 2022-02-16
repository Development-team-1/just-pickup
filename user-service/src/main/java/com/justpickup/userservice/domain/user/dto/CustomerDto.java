package com.justpickup.userservice.domain.user.dto;

import com.justpickup.userservice.domain.user.entity.Customer;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomerDto extends UserDto {

    public CustomerDto(Customer customer) {
        super(customer);
    }

    @Builder
    public CustomerDto(Long id, String email, String password, String name,
                       String phoneNumber, String dtype, String refreshTokenId) {
        super(id, email, password, name, phoneNumber, dtype, refreshTokenId);
    }
}
