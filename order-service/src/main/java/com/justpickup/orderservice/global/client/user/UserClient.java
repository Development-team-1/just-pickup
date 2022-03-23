package com.justpickup.orderservice.global.client.user;

import com.justpickup.orderservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@FeignClient("USER-SERVICE")
public interface UserClient {

    @GetMapping("/customer/{userId}")
    Result<GetCustomerResponse> getCustomerById(@PathVariable("userId") Long userId);

    @GetMapping("/customers/{userIds}")
    Result<List<GetCustomerResponse>> getCustomers(@PathVariable("userIds") Iterable<Long> userIds);

    default Map<Long, String> getUserNameMap(Iterable<Long> userIds) {
        if (!userIds.iterator().hasNext()) return null;
        List<GetCustomerResponse> userResponses = this.getCustomers(userIds).getData();
        return userResponses.stream()
                .collect(
                        toMap(GetCustomerResponse::getUserId, GetCustomerResponse::getUserName)
                );
    }
}
