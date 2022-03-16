package com.justpickup.orderservice.global.client.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStoreReseponse {
    private Long id;
    private String name;
    private String phoneNumber;
}