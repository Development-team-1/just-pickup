package com.justpickup.storeservice.domain.store.web;

import com.justpickup.storeservice.domain.store.dto.SearchStoreCondition;
import com.justpickup.storeservice.domain.store.dto.SearchStoreResult;
import com.justpickup.storeservice.domain.store.service.StoreService;
import com.justpickup.storeservice.global.dto.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("api/customer/store")
@RequiredArgsConstructor
public class StoreCustomerApiController {

    private final StoreService storeService;

    @GetMapping("/favorite")
    public ResponseEntity getFavoriteStore(@Valid SearchStoreCondition condition,
                                           @RequestHeader(value = "user-id") String userId){

        List<SearchStoreResult> favoriteStore = storeService.findFavoriteStore(condition,Long.parseLong(userId));

        List<StoreCustomerApiController.SearchStoreResponse> searchStoreResponse
                = favoriteStore.stream().map(SearchStoreResponse::new).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(Result.createSuccessResult(searchStoreResponse));
    }

    @Data @NoArgsConstructor
    static class SearchStoreResponse {
        private Long id;
        private String name;
        private String distance;

        public SearchStoreResponse(SearchStoreResult storeResult) {
            this.id = storeResult.getStoreId();
            this.name = storeResult.getStoreName();
            this.distance = storeResult.convertDistanceToString();
        }
    }





}
