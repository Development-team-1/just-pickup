package com.justpickup.ownerapigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OwnerApigatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwnerApigatewayServiceApplication.class, args);
	}

}
