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

        List<FavoriteStoreResponse> searchStoreResponse
                = favoriteStore.stream().map(FavoriteStoreResponse::new).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(Result.createSuccessResult(searchStoreResponse));
    }

    @Data @NoArgsConstructor
    static class FavoriteStoreResponse {
        private Long id;
        private String name;
        private String distance;
        private Long favoriteCounts;

        public FavoriteStoreResponse(SearchStoreResult storeResult) {
            this.id = storeResult.getStoreId();
            this.name = storeResult.getStoreName();
            this.distance = storeResult.convertDistanceToString();
            this.favoriteCounts = storeResult.getFavoriteCounts();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Result> searchStore(@Valid SearchStoreCondition condition,
                                              @PageableDefault(page = 0, size = 2) Pageable pageable) {
        SliceImpl<SearchStoreResult> searchStoreScroll = storeService.findSearchStoreScroll(condition, pageable);

        SearchStoreResponse searchStoreResponse =
                new SearchStoreResponse(searchStoreScroll.getContent(), searchStoreScroll.hasNext());

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(searchStoreResponse));
    }

    @Data
    @NoArgsConstructor
    static class SearchStoreResponse {
        private List<StoreDto> stores;
        private boolean hasNext;

        @Data @AllArgsConstructor
        static class StoreDto {
            private Long id;
            private String name;
            private String distance;
            private Long favoriteCounts;
        }

        public SearchStoreResponse(List<SearchStoreResult> content, boolean hasNext) {
            this.stores = content.stream()
                    .map(result ->
                            new StoreDto(
                                    result.getStoreId(), result.getStoreName(),
                                    result.convertDistanceToString(), result.getFavoriteCounts())
                    )
                    .collect(Collectors.toList());
            this.hasNext = hasNext;
        }
    }
}
