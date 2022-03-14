package com.justpickup.orderservice.domain.orderItem.entity;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.orderItemOption.entity.OrderItemOption;
import com.justpickup.orderservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
    private List<OrderItemOption> orderItemOptions = new ArrayList<>();

    private Long price;

    private Long count;

    public void setOrder(Order order) {
        this.order = order;
    }

    public void addOrderItemOption(OrderItemOption orderItemOption) {
        this.orderItemOptions.add(orderItemOption);
        orderItemOption.setOrderItem(this);
    }

    public static OrderItem of(Long itemId, Long price, Long count, OrderItemOption... orderItemOptions) {
        OrderItem orderItem = new OrderItem();
        orderItem.itemId = itemId;
        orderItem.price = price;
        orderItem.count = count;
        for (OrderItemOption orderItemOption : orderItemOptions) {
            orderItem.addOrderItemOption(orderItemOption);
        }
        return orderItem;
    }

    public static OrderItem of(Long itemId, Long price, Long count, List<OrderItemOption> orderItemOptions) {
        OrderItem orderItem = new OrderItem();
        orderItem.itemId = itemId;
        orderItem.price = price;
        orderItem.count = count;
        for (OrderItemOption orderItemOption : orderItemOptions) {
            orderItem.addOrderItemOption(orderItemOption);
        }
        return orderItem;
    }

    /**
     * 주문상품 전체 가격 조회
     */
    public long getTotalPrice() {
        return price * count;
    }
}
