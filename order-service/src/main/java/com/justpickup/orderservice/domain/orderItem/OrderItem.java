package com.justpickup.orderservice.domain.orderItem;

import com.justpickup.orderservice.domain.order.Order;
import com.justpickup.orderservice.global.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @Id    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    private Long item_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long price;

}
