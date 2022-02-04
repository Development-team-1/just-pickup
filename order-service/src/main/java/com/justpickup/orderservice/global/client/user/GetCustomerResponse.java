package com.justpickup.orderservice.global.client.user;

import lombok.Data;

@Data
public class GetCustomerResponse {
    private Long userId;
    private String userName;
    private String phoneNumber;
}
