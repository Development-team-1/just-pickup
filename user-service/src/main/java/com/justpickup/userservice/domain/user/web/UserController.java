package com.justpickup.userservice.domain.user.web;

import com.justpickup.userservice.domain.user.dto.CustomerDto;
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

    @GetMapping("/afterLogin")
    public ResponseEntity afterLogin(@RequestParam String code){

        log.info("로그인 성공 - code::"+ code);

        String accessToken = userService.authByGithub(code);




        // access token 발급받는다 -> User DB에 저장한다.  github ID 를 db에 저장한다.
        // Oauth type, id 저장 . password
        //
        // https://api.github.com/user
        // 이미 있는 github iD? 회원 가입한 사용자.
        //


        return ResponseEntity.ok(accessToken);
    }

}
