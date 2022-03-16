package com.justpickup.orderservice.global.client.store;

import com.justpickup.orderservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient("STORE-SERVICE")
public interface StoreClient {

    @GetMapping("/item/{itemId}")
    Result<GetItemResponse> getItem(@PathVariable("itemId") Long itemId);

    @GetMapping("/items/{itemIds}")
    Result<List<GetItemsResponse>> getItems(@PathVariable("itemIds") Iterable<Long> itemIds);

    @GetMapping("/api/owner/store/")
    Result<StoreByUserIdResponse> getStoreByUserId(@RequestHeader(value="user-id") Long userId);

    @GetMapping("/stores/{storeId}")
    Result<List<GetStoreResponse>> getStoreAllById(@PathVariable("storeId") Iterable<Long> storeIds);
}
