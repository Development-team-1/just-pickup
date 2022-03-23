package com.justpickup.orderservice.global.client.store;

import com.justpickup.orderservice.global.client.user.GetCustomerResponse;
import com.justpickup.orderservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

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
  
    @GetMapping("/store/{storeId}")
    Result<GetStoreResponse> getStore(@PathVariable(value = "storeId") String storeId);

    @GetMapping("/api/customer/items/{itemId}")
    Result<List<GetItemResponse>> getItemAndItemOptions(@PathVariable(value = "itemId") Iterable<Long> itemIds);

    default Map<Long, String> getStoreNameMap(Set<Long> storeIds) {
        if (!storeIds.iterator().hasNext()) return null;
        List<GetStoreResponse> storeResponses = this.getStoreAllById(storeIds).getData();
        return storeResponses.stream()
                .collect(
                        toMap(GetStoreResponse::getId, GetStoreResponse::getName)
                );
    }

    default Map<Long, String> getItemNameMap(Iterable<Long> itemIds) {
        if (!itemIds.iterator().hasNext()) return null;
        List<GetItemsResponse> itemResponses = this.getItems(itemIds).getData();
        return itemResponses.stream()
                .collect(
                        toMap(GetItemsResponse::getId, GetItemsResponse::getName)
                );
    }

    default Map<Long, GetItemResponse> getItemAndItemOptionMap(Iterable<Long> itemIds) {
        if (!itemIds.iterator().hasNext()) return null;
        List<GetItemResponse> responses = this.getItemAndItemOptions(itemIds).getData();
        return responses.stream()
                .collect(
                        toMap(GetItemResponse::getId, getItemsResponse -> getItemsResponse)
                );
    }
}
