package com.justpickup.storeservice.domain.store.web;

import com.justpickup.storeservice.domain.store.dto.StoreByUserIdDto;
import com.justpickup.storeservice.domain.store.service.StoreService;
import com.justpickup.storeservice.global.dto.Result;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class StoreOwnerApiController {

    private final StoreService storeService;

    @GetMapping("/api/store")
    public ResponseEntity<Result> getStoreByUserId(@RequestHeader("user-id") String userHeader) {
        Long userId = Long.valueOf(userHeader);

        StoreByUserIdDto dto = storeService.findStoreByUserId(userId);

        return ResponseEntity.ok(Result.createSuccessResult(dto));
    }

    @Data
    static class StoreByUserIdResponse {
        private Long id;
        private String name;

        public StoreByUserIdResponse(StoreByUserIdDto dto) {
            this.id = dto.getId();
            this.name = dto.getName();
        }

    }
}
