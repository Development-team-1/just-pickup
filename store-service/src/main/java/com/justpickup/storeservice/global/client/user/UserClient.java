package com.justpickup.storeservice.global.client.user;

import com.justpickup.storeservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("USER-SERVICE")
public interface UserClient {

    @GetMapping("/customer/{userId}")
    Result<GetCustomerResponse> getCustomerById(@PathVariable("userId") Long userId);
}
