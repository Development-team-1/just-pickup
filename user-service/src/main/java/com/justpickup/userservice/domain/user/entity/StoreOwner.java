package com.justpickup.userservice.domain.user.entity;

import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "store_owner")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreOwner extends User {
    private String businessNumber;

    public StoreOwner(String email, String password, String name, String phoneNumber,
                      String businessNumber, String refreshTokenId) {
        super(email, password, name, phoneNumber, refreshTokenId);
        this.businessNumber = businessNumber;
    }
}
