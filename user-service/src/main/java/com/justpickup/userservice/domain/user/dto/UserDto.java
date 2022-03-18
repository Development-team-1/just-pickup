package com.justpickup.userservice.domain.user.dto;

import com.justpickup.userservice.domain.user.entity.Customer;
import lombok.Getter;

@Getter
public abstract class UserDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String dtype;

    // == 생성 메소드 == //
    public UserDto(Customer customer) {
        this.id = customer.getId();
        this.email = customer.getEmail();
        this.password = customer.getPassword();
        this.name = customer.getName();
        this.phoneNumber = customer.getPhoneNumber();
    }

    public UserDto(Long id, String email, String password, String name, String phoneNumber,
                   String dtype) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dtype = dtype;
    }
}
