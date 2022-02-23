package com.justpickup.userservice.domain.user.web;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.entity.Customer;
import com.justpickup.userservice.domain.user.service.UserService;
import com.justpickup.userservice.global.dto.Result;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

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



}
