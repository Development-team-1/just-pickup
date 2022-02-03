package com.justpickup.orderservice.global.client.user;

import com.justpickup.orderservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE", url = "127.0.0.1:8001/user-service")
public interface UserClient {

    @GetMapping("/customer/{userId}")
    Result<GetCustomerResponse> getUser(@PathVariable("userId") Long userId);
}
