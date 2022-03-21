package com.justpickup.userservice.domain.user.dto;

import com.justpickup.userservice.domain.user.entity.StoreOwner;
import com.justpickup.userservice.global.client.store.PostStoreRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Getter
public class PostOwnerDto {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String businessNumber;

    @Builder
    public PostOwnerDto(String email, String password, String name, String phoneNumber, String businessNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.businessNumber = businessNumber;
    }

    public StoreOwner toStoreOwner() {
        return new StoreOwner(this.email, this.password, this.name, this.phoneNumber, this.businessNumber);
    }

}
