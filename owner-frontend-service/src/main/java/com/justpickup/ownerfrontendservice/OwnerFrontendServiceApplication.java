package com.justpickup.ownerfrontendservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OwnerFrontendServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OwnerFrontendServiceApplication.class, args);
    }

}
