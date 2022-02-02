package com.justpickup.orderservice.domain.orderItem.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class OrderItemDto {

    private Long id;

    private Long itemId;

    // private List<OrderItemOptionDto> orderItemOptionDtoList;

    private Long price;

    private Long count;

    @Builder
    public OrderItemDto(Long id, Long itemId, Long price, Long count) {
        this.id = id;
        this.itemId = itemId;
        this.price = price;
        this.count = count;
    }
}
