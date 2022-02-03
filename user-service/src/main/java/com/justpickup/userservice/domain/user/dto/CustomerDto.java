package com.justpickup.userservice.domain.user.dto;

import com.justpickup.userservice.domain.user.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerDto extends UserDto {

    public CustomerDto(Customer customer) {
        super(customer);
    }
}
