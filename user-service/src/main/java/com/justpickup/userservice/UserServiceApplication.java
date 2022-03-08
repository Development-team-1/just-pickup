package com.justpickup.userservice;

import com.justpickup.userservice.domain.user.dto.StoreOwnerDto;
import com.justpickup.userservice.domain.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            StoreOwnerDto park = StoreOwnerDto.builder()
                    .email("test@gmail.com").password("1234").name("Park").phoneNumber("010-1234-5678")
                    .build();
            userService.saveStoreOwner(park);
        };
    }
}
