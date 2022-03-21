package com.justpickup.storeservice.domain.store.web;

import com.justpickup.storeservice.domain.store.dto.PostStoreDto;
import com.justpickup.storeservice.domain.store.dto.StoreByUserIdDto;
import com.justpickup.storeservice.domain.store.service.StoreService;
import com.justpickup.storeservice.global.dto.Result;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class StoreOwnerApiController {

    private final StoreService storeService;

    @GetMapping("/owner/store")
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

    @PostMapping("/owner/store")
    public ResponseEntity<Result> postStore(@Valid @RequestBody PostStoreRequest postStoreRequest,
                                            @RequestHeader(value="user-id") String userHeader) {
        Long userId = Long.valueOf(userHeader);

        storeService.saveStore(postStoreRequest.toPostStoreDto(userId));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    static class PostStoreRequest {
        @NotEmpty
        private String name;
        @NotEmpty
        private String phoneNumber;
        @NotEmpty
        private String address;
        @NotEmpty
        private String zipcode;
        @NotNull
        private Double latitude;
        @NotNull
        private Double longitude;

        public PostStoreDto toPostStoreDto(Long userId) {
            PostStoreDto._PostStoreAddress address =
                    PostStoreDto._PostStoreAddress.builder().address(this.address).zipcode(this.zipcode).build();

            PostStoreDto._PostStoreMap map =
                    PostStoreDto._PostStoreMap.builder().latitude(this.latitude).longitude(this.longitude).build();

            return PostStoreDto.builder()
                    .name(this.name)
                    .phoneNumber(this.phoneNumber)
                    .userId(userId)
                    .address(address)
                    .map(map)
                    .build();
        }
    }
}
