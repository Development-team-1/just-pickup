package com.justpickup.orderservice.global.client.store;

import com.justpickup.orderservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "STORE-SERVICE", url = "127.0.0.1:8001/store-service")
public interface StoreClient {

    @GetMapping("/item/{itemId}")
    Result<GetItemResponse> getItem(@PathVariable("itemId") Long itemId);
}
