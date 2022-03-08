package com.justpickup.orderservice.domain.orderItemOption.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemOptionDto {
    private Long id;

    public OrderItemOptionDto(Long id) {
        this.id = id;
    }
}
