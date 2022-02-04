package com.justpickup.orderservice.global.client.store;

import com.justpickup.orderservice.global.entity.Yn;
import lombok.Data;

@Data
public class GetItemResponse {
    private Long id;
    private String name;
    private Yn salesYn;
    private Long price;
}
