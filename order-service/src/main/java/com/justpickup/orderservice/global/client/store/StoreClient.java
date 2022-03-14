package com.justpickup.orderservice.global.client.store;

import com.justpickup.orderservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("STORE-SERVICE")
public interface StoreClient {

    @GetMapping("/item/{itemId}")
    Result<GetItemResponse> getItem(@PathVariable("itemId") Long itemId);

    @GetMapping("/api/owner/store/")
    Result<StoreByUserIdResponse> getStoreByUserId(@RequestHeader(value="user-id") Long userId);

}
