package com.justpickup.userservice.domain.user.dto;

import com.justpickup.userservice.domain.user.entity.Customer;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private Long id;
    private String password;
    private String name;
    private String phoneNumber;

    // == 생성 메소드 == //
    public UserDto(Customer customer) {
        this.id = customer.getId();
        this.password = customer.getPassword();
        this.name = customer.getName();
        this.phoneNumber = customer.getPhoneNumber();
    }

    public UserDto(Long id, String password, String name, String phoneNumber) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
