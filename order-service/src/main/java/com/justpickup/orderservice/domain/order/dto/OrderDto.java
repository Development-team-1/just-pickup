package com.justpickup.orderservice.domain.order.dto;

import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter @NoArgsConstructor
public class OrderDto {
    private Long id;

    private Long userId;

    private Long userCouponId;

    private Long orderPrice;

    private LocalDateTime orderTime;

    private Long usedPoint;

    private OrderStatus orderStatus;

    // private TransactionDto transactionDto;

    private List<OrderItemDto> orderItemDtoList;

    @Builder
    public OrderDto(Long id, Long userId, Long userCouponId, Long orderPrice, LocalDateTime orderTime, Long usedPoint,
                    OrderStatus orderStatus, List<OrderItemDto> orderItemDtoList) {
        this.id = id;
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.orderPrice = orderPrice;
        this.orderTime = orderTime;
        this.usedPoint = usedPoint;
        this.orderStatus = orderStatus;
        this.orderItemDtoList = orderItemDtoList;
    }
}
