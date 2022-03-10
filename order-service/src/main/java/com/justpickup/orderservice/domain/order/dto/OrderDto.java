package com.justpickup.orderservice.domain.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
public class OrderDto {
    private Long id;

    private Long userId;

    private String userName;

    private Long userCouponId;

    private Long orderPrice;

    private Long storeId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime orderTime;

    private Long usedPoint;

    private OrderStatus orderStatus;

    // private TransactionDto transactionDto;

    private List<OrderItemDto> orderItemDtoList;

    @Builder
    public OrderDto(Long id, Long userId, Long userCouponId, Long orderPrice, LocalDateTime orderTime, Long storeId,
                    Long usedPoint, OrderStatus orderStatus, List<OrderItemDto> orderItemDtoList) {
        this.id = id;
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.orderPrice = orderPrice;
        this.orderTime = orderTime;
        this.storeId = storeId;
        this.usedPoint = usedPoint;
        this.orderStatus = orderStatus;
        this.orderItemDtoList = orderItemDtoList;
    }

    // == 생성 메소드 == //
    public static OrderDto createPrimitiveField(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .userCouponId(order.getUserCouponId())
                .orderPrice(order.getOrderPrice())
                .orderTime(order.getOrderTime())
                .storeId(order.getStoreId())
                .usedPoint(order.getUsedPoint())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    public static OrderDto createFullField(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItemDto> orderItemDtoList = orderItems.stream()
                .map(OrderItemDto::createPrimitiveField)
                .collect(Collectors.toList());

        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .userCouponId(order.getUserCouponId())
                .orderPrice(order.getOrderPrice())
                .orderTime(order.getOrderTime())
                .storeId(order.getStoreId())
                .usedPoint(order.getUsedPoint())
                .orderStatus(order.getOrderStatus())
                .orderItemDtoList(orderItemDtoList)
                .build();
    }




    // == 변수 변경 메소드 == //
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
