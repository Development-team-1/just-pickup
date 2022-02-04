package com.justpickup.orderservice.domain.orderItem.entity;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.domain.orderItemOption.entity.OrderItemOption;
import com.justpickup.orderservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @Id    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "orderItem")
    private List<OrderItemOption> orderItemOptions;

    private Long price;

    private Long count;

}
