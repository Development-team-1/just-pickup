package com.justpickup.notificationservice.global.client.store;

import com.justpickup.notificationservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("STORE-SERVICE")
public interface StoreClient {

    @GetMapping("/store/{storeId}")
    Result<GetStoreResponse> getStore(@PathVariable(value = "storeId") String storeId);
}
