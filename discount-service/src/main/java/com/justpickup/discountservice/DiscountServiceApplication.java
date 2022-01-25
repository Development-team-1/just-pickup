package com.justpickup.discountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DiscountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscountServiceApplication.class, args);
    }

}
