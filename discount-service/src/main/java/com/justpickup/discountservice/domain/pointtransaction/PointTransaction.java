package com.justpickup.discountservice.domain.pointtransaction;

import com.justpickup.discountservice.domain.point.Point;
import com.justpickup.discountservice.global.entity.BaseEntity;
import com.justpickup.discountservice.global.entity.Photo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name = "point_transaction")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "point_transaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

}
