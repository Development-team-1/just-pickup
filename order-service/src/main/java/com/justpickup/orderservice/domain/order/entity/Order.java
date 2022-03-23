package com.justpickup.orderservice.domain.order.entity;

import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.domain.transaction.entity.Transaction;
import com.justpickup.orderservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@EntityListeners(value = {OrderListener.class})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private Long userId;

    private Long userCouponId;

    private Long storeId;

    private long orderPrice;

    private LocalDateTime orderTime;

    private long usedPoint;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Transaction transaction;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public static Order of(Long userId, Long userCouponId, Long storeId, OrderItem orderItem) {
        Order order = new Order();
        order.userId = userId;
        order.userCouponId = userCouponId;
        order.storeId = storeId;

        order.addOrderItem(orderItem);

        order.usedPoint = 0L;
        order.orderStatus = OrderStatus.PENDING;
        order.orderTime = LocalDateTime.now();
        return order;
    }

    public static Order of(Long userId, Long userCouponId, Long storeId, List<OrderItem> orderItems) {
        Order order = new Order();
        order.userId = userId;
        order.userCouponId = userCouponId;
        order.storeId = storeId;

        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        order.usedPoint = 0L;
        order.orderStatus = OrderStatus.PENDING;
        order.orderTime = LocalDateTime.now();
        return order;
    }

    public Order addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        this.orderPrice += orderItem.getTotalPrice();
        orderItem.setOrder(this);
        return this;
    }

    public Order deleteOrderItem(OrderItem orderItem) {
        this.orderPrice -= orderItem.getTotalPrice();
        this.orderItems.remove(orderItem);

        return this;
    }

    public void setOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    public void placed() {
        this.orderStatus = OrderStatus.PLACED;
    }

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void fail() {
        this.orderStatus = OrderStatus.FAILED;
    }
}
