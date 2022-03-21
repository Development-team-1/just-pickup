package com.justpickup.userservice.global.client.store;

import com.justpickup.userservice.global.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;

@FeignClient("STORE-SERVICE")
public interface StoreClient {

    @PostMapping("/api/owner/store")
    ResponseEntity<Result> postStore(@Valid @RequestBody PostStoreRequest postStoreRequest,
                                     @RequestHeader("user-id") Long userId);
}
