package com.justpickup.storeservice.global.client.user;

import lombok.Data;

@Data
public class GetCustomerResponse {
    private Long userId;
    private String userName;
    private String phoneNumber;
}
