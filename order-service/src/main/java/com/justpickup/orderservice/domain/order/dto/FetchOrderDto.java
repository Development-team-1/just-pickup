package com.justpickup.orderservice.domain.order.dto;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.domain.orderItem.entity.OrderItem;
import com.justpickup.orderservice.domain.orderItemOption.dto.OrderItemOptionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class FetchOrderDto {
    private Long id;

    private Long userId;

    private String userName;

    private Long userCouponId;

    private Long orderPrice;

    private Long storeId;

    private LocalDateTime orderTime;

    private Long usedPoint;

    private OrderStatus orderStatus;

    private List<OrderItemDto> orderItemDtoList;


    public FetchOrderDto(Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.orderPrice = order.getOrderPrice();
        this.storeId = order.getStoreId();
        this.orderItemDtoList = order.getOrderItems().stream()
                .map(orderItem -> OrderItemDto.of(orderItem.getId(),orderItem.getItemId(),orderItem.getPrice(),orderItem.getCount(),orderItem.getOrderItemOptions().stream().map(orderItemOption -> new OrderItemOptionDto(orderItemOption.getId())).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

}
