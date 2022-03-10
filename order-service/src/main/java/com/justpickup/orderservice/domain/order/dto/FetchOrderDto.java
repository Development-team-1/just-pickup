package com.justpickup.orderservice.domain.order.dto;

import com.justpickup.orderservice.domain.order.entity.Order;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.domain.orderItemOption.dto.OrderItemOptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FetchOrderDto {
    private Long id;

    private Long userId;

    private Long orderPrice;

    private Long storeId;

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
