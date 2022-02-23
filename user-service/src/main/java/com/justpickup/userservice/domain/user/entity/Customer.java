package com.justpickup.userservice.domain.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends User {
    @Enumerated(EnumType.STRING)
    private AuthType oauthType;

    public Customer(String email, String password, String name, String phoneNumber, AuthType oauthType) {
        super(email, password, name, phoneNumber);
        this.dtype = Customer.class.getSimpleName();
        this.oauthType = oauthType;
    }
}
