package com.justpickup.orderservice.domain.transaction;

import com.justpickup.orderservice.domain.order.Order;
import com.justpickup.orderservice.global.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
@Getter
@NoArgsConstructor
public class Transaction extends BaseEntity {

    @Id    @GeneratedValue
    @Column(name = "transaction_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}
