package com.justpickup.userservice.domain.user.web;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;
import com.justpickup.userservice.domain.user.service.UserService;
import com.justpickup.userservice.global.dto.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/customer/")
    public ResponseEntity getCustomerByToken(@Valid @RequestHeader(value = "user-id") String userId ) {

        CustomerDto customerDto = userService.findCustomerByUserId(Long.parseLong(userId));

        GetCustomerByTokenResponse getCustomerByTokenResponse = new GetCustomerByTokenResponse(customerDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(getCustomerByTokenResponse));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class GetCustomerByTokenResponse {
        private Long userId;
        private String email;
        private String userName;
        private String phoneNumber;

        public GetCustomerByTokenResponse(CustomerDto customerDto) {
            this.userId = customerDto.getId();
            this.email = customerDto.getEmail();
            this.userName = customerDto.getName();
            this.phoneNumber = customerDto.getPhoneNumber();
        }
    }


    @GetMapping("/customer/{userId}")
    public ResponseEntity getCustomer(@Valid @PathVariable("userId") Long userId) {

        CustomerDto customerDto = userService.findCustomerByUserId(userId);

        GetCustomerResponse getCustomerResponse = new GetCustomerResponse(customerDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(getCustomerResponse));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class GetCustomerResponse {
        private Long userId;
        private String userName;
        private String phoneNumber;

        public GetCustomerResponse(CustomerDto customerDto) {
            this.userId = customerDto.getId();
            this.userName = customerDto.getName();
            this.phoneNumber = customerDto.getPhoneNumber();
        }
    }

    @PostMapping("/store-owner")
    public ResponseEntity<Result> joinStoreOwner(@Valid @RequestBody JoinStoreOwnerRequest joinRequest) {
        // 회원 가입
        userService.saveStoreOwner(joinRequest.toStoreOwnerDto());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Result.createSuccessResult(null));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class JoinStoreOwnerRequest {
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

        public StoreOwnerDto toStoreOwnerDto() {
            return StoreOwnerDto.builder()
                    .email(email).password(password).name(name)
                    .password(password).businessNumber(businessNumber)
                    .build();
        }
    }


}
