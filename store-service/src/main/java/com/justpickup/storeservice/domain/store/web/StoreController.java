package com.justpickup.storeservice.domain.store.web;

import com.justpickup.storeservice.domain.store.dto.StoreDto;
import com.justpickup.storeservice.domain.store.service.StoreService;
import com.justpickup.storeservice.global.dto.Result;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{storeId}")
    public ResponseEntity<Result> getStore(@PathVariable("storeId") Long storeId) {
        StoreDto storeDto = storeService.findStore(storeId);

        GetStoreResponse getStoreResponse = new GetStoreResponse(storeDto);
        return ResponseEntity.ok(Result.createSuccessResult(getStoreResponse));
    }

    @Data @NoArgsConstructor
    static class GetStoreResponse {
        private Long id;
        private String name;
        private String phoneNumber;

        public GetStoreResponse(StoreDto storeDto) {
            this.id = storeDto.getId();
            this.name = storeDto.getName();
            this.phoneNumber = storeDto.getPhoneNumber();
        }
    }
}
