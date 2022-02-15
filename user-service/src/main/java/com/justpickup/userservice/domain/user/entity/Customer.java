package com.justpickup.userservice.domain.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends User {
    private AuthType oauthType;
    private String oauthId;
}
