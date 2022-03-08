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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private Long userId;

    private Long userCouponId;

    private Long storeId;

    private Long orderPrice;

    private LocalDateTime orderTime;

    private Long usedPoint;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Transaction transaction;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    public static Order of(Long userId, Long userCouponId, Long storeId, Long orderPrice,
                              Transaction transaction, OrderItem... orderItems) {
        Order order = new Order();
        order.userId = userId;
        order.userCouponId = userCouponId;
        order.storeId = storeId;
        order.orderPrice = orderPrice;

        order.setTransaction(transaction);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.usedPoint = 0L;
        order.orderStatus = OrderStatus.PLACED;
        order.orderTime = LocalDateTime.now();
        return order;
    }

    public static Order of(Long userId, Long userCouponId, Long storeId, Long orderPrice,
                            OrderItem orderItem) {
        Order order = new Order();
        order.userId = userId;
        order.userCouponId = userCouponId;
        order.storeId = storeId;
        order.orderPrice = orderPrice;

        order.addOrderItem(orderItem);

        order.usedPoint = 0L;
        order.orderStatus = OrderStatus.PLACED;
        order.orderTime = LocalDateTime.now();
        return order;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        transaction.setOrder(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
