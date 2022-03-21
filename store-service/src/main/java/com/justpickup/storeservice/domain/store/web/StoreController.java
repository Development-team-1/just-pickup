package com.justpickup.storeservice.domain.store.web;

import com.justpickup.storeservice.domain.store.dto.StoreDto;
import com.justpickup.storeservice.domain.store.service.StoreService;
import com.justpickup.storeservice.global.dto.Result;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/store/{storeId}")
    public ResponseEntity<Result> getStore(@PathVariable("storeId") Long storeId) {
        StoreDto storeDto = storeService.findStoreById(storeId);

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

    @GetMapping("/stores/{storeIds}")
    public ResponseEntity<Result> getStores(@PathVariable("storeIds") List<Long> storeIds) {

        List<StoreDto> storeDtoList = storeService.findStoreAllById(storeIds);

        List<GetStoreResponse> responses = storeDtoList.stream()
                .map(GetStoreResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Result.createSuccessResult(responses));
    }
}
