package com.justpickup.userservice.domain.user.web;

import com.justpickup.userservice.domain.user.dto.PostOwnerDto;
import com.justpickup.userservice.domain.user.dto.PostStoreDto;
import com.justpickup.userservice.domain.user.service.UserService;
import com.justpickup.userservice.global.dto.Result;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserOwnerApiController {

    private final UserService userService;

    @PostMapping("/owner/store-owner")
    public ResponseEntity<Result> postStoreOwner(@Valid @RequestBody PostStoreOwnerRequest postStoreOwnerRequest) {

        userService.saveStoreOwner(
                postStoreOwnerRequest.toPostOwnerDto(), postStoreOwnerRequest.toPostStoreDto()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Result.createSuccessResult(null));
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    static class PostStoreOwnerRequest {
        @Email(message = "email 형식이 아닙니다.")
        @NotEmpty
        private String email;
        @NotEmpty
        private String password;
        @NotEmpty
        private String name;
        @NotEmpty
        private String phoneNumber;
        @NotEmpty
        private String businessNumber;

        @NotEmpty
        private String storeName;
        @NotEmpty
        private String storePhoneNumber;
        @NotEmpty
        private String address;
        @NotEmpty
        private String zipcode;
        @NotNull
        private Double latitude;
        @NotNull
        private Double longitude;

        public PostOwnerDto toPostOwnerDto() {
            return PostOwnerDto.builder()
                    .email(this.email)
                    .password(this.password)
                    .name(this.name)
                    .phoneNumber(this.phoneNumber)
                    .businessNumber(this.businessNumber)
                    .build();
        }

        public PostStoreDto toPostStoreDto() {
            return PostStoreDto.builder()
                    .name(this.storeName)
                    .phoneNumber(this.phoneNumber)
                    .address(this.address)
                    .zipcode(this.zipcode)
                    .latitude(this.latitude)
                    .longitude(this.longitude)
                    .build();
        }
    }
}
