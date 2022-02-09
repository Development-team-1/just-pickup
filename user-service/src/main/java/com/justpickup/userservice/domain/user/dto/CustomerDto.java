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
    public CustomerDto(Long id, String password, String name, String phoneNumber) {
        super(id, password, name, phoneNumber);
    }
}
