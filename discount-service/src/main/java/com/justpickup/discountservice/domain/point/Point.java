package com.justpickup.discountservice.domain.point;

import com.justpickup.discountservice.domain.pointtransaction.PointTransaction;
import com.justpickup.discountservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table(name = "point")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "point_id")
    private Long id;

    /*userDb.users.user_id*/
    private Long userId;

    private Long amount;

    @OneToMany(mappedBy = "point",fetch = FetchType.LAZY)
    private List<PointTransaction> pointTransactions;


}
