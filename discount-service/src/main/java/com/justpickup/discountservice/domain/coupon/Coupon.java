package com.justpickup.discountservice.domain.coupon;

import com.justpickup.discountservice.domain.point.Point;
import com.justpickup.discountservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "coupon")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;

    private String name;

    private LocalDate expiredDate;

    private String couponNumber;

    private Long quantity;

}
