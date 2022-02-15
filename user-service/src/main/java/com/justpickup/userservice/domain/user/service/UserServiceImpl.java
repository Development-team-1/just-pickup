package com.justpickup.userservice.domain.user.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.justpickup.userservice.domain.user.dto.CustomerDto;
import com.justpickup.userservice.domain.user.entity.Customer;
import com.justpickup.userservice.domain.user.exception.NotExistUserException;
import com.justpickup.userservice.domain.user.repository.CustomerRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final CustomerRepository customerRepository;
    private final Environment env;

    @Override
    public CustomerDto findCustomerByUserId(Long userId) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new NotExistUserException("존재하지 않는 사용자 입니다."));

        return new CustomerDto(customer);
    }

    @Override
    public String authByGithub(String code) {
        RestTemplate restTemplate = new RestTemplate();
        AuthRequest authRequest = new AuthRequest();
        authRequest.setCode(code);
        authRequest.setClient_id(env.getProperty("oauth.github.client-id"));
        authRequest.setClient_secret(env.getProperty("oauth.github.client-password"));
        String accessToken = restTemplate.postForObject("https://github.com/login/oauth/access_token", authRequest, String.class);

        accessToken = accessToken.substring(accessToken.indexOf("access_token=")+13);
        accessToken = accessToken.substring(0,accessToken.indexOf("&"));

        log.info("accessToken::" +accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+accessToken);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, request, String.class);
        AuthResponse authResponse=null;
        try {
            authResponse = new ObjectMapper().readValue(response.getBody(), AuthResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info(authResponse.getName());


        log.info("access_token::" + accessToken);
        return authResponse.getName();
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthResponse{
        private String id;
        private String name;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class AuthRequest {
        private String code;
        private String client_id;
        private String client_secret;
    }

}
