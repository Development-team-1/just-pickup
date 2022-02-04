package com.justpickup.userservice.domain.user.web;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/customer/{userId}")
    public ResponseEntity getCustomer(@PathVariable("userId") Long userId) {

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

}
