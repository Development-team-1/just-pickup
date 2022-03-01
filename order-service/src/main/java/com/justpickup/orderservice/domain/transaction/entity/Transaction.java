package com.justpickup.orderservice.domain.transaction.entity;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction extends BaseEntity {

    @Id    @GeneratedValue
    @Column(name = "transaction_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }

    public static Transaction of() {
        Transaction transaction = new Transaction();
        return transaction;
    }

}
