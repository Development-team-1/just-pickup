package com.justpickup.discountservice.domain.usercoupon;

import com.justpickup.discountservice.domain.coupon.Coupon;
import com.justpickup.discountservice.global.entity.BaseEntity;
import com.justpickup.discountservice.global.entity.Yn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "user_coupon")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    /*userDb.users.user_id*/
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Yn useYn;






}
