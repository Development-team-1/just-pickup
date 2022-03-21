package com.justpickup.orderservice.global.client.store;

import lombok.Data;

@Data
public class ItemOptionsResponse {
    private Long id;
    private OptionType optionType;
    private String name;
}
