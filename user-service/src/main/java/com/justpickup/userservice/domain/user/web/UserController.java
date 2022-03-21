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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/customer")
    public ResponseEntity getCustomerByToken(@Valid @RequestHeader(value="user-id") String userId) {

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
    public ResponseEntity getCustomer(@PathVariable("userId") Long userId) {

        CustomerDto customerDto = userService.findCustomerByUserId(userId);

        GetCustomerResponse getCustomerResponse = new GetCustomerResponse(customerDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(getCustomerResponse));
    }

    @GetMapping("/customers/{userIds}")
    public ResponseEntity getCustomers(@PathVariable List<Long> userIds) {

        List<CustomerDto> customers = userService.findCustomerByUserIds(userIds);

        List<GetCustomerResponse> responses = customers
                .stream()
                .map(GetCustomerResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Result.createSuccessResult(responses));
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

    @GetMapping("/store-owner")
    public ResponseEntity<Result> getStoreOwnerByToken(@RequestHeader(value="user-id") String userHeader) {
        Long userId = Long.valueOf(userHeader);

        StoreOwnerDto storeOwnerDto = userService.findOwnerById(userId);

        return ResponseEntity.ok(Result.createSuccessResult(storeOwnerDto));
    }

    @Data
    static class StoreOwnerByTokenResponse {
        private Long id;
        private String name;

        public StoreOwnerByTokenResponse(StoreOwnerDto dto) {
            this.id = dto.getId();
            this.name = dto.getName();
        }
    }
}
