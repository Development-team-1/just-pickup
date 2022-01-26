package com.justpickup.orderservice.domain.orderItemOption;

import com.justpickup.orderservice.domain.orderItem.OrderItem;
import com.justpickup.orderservice.global.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
public class OrderItemOption extends BaseEntity {

    @Id    @GeneratedValue
    @Column(name = "order_item_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    private Long itemOptionId;

}
