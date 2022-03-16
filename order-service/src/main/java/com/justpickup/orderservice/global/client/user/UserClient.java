package com.justpickup.orderservice.global.client.user;

import com.justpickup.orderservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("USER-SERVICE")
public interface UserClient {

    @GetMapping("/customer/{userId}")
    Result<GetCustomerResponse> getCustomerById(@PathVariable("userId") Long userId);

    @GetMapping("/customers/{userIds}")
    Result<List<GetCustomerResponse>> getCustomers(@PathVariable("userIds") Iterable<Long> userIds);
}
