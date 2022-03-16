package com.justpickup.orderservice.global.client.store;

import lombok.Data;

@Data
public class GetStoreResponse {
    private Long id;
    private String name;
    private String phoneNumber;
}
