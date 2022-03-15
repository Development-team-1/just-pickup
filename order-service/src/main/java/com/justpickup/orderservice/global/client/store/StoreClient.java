package com.justpickup.orderservice.global.client.store;

import com.justpickup.orderservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("store-service")
public interface StoreClient {

    @GetMapping("/store/{storeId}")
    Result<GetStoreReseponse> getStore(@PathVariable(value = "storeId") String storeId);

    @GetMapping("/api/customer/items/{itemId}")
    Result<List<GetItemResponse>> getItemAndItemOptions(@PathVariable(value = "itemId") List<Long> itemIds);

}
