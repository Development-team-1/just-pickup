package com.justpickup.userservice.domain.user.entity;

import com.justpickup.userservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private String refreshTokenId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(insertable = false,updatable = false)
    private String dtype;

    public User(String email, String password, String name, String phoneNumber, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public User(String email, String password, String name, String phoneNumber, String refreshTokenId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.refreshTokenId = refreshTokenId;
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshTokenId = refreshToken;
    }

    public void deleteRefreshToken() {
        this.refreshTokenId = null;
    }
}
