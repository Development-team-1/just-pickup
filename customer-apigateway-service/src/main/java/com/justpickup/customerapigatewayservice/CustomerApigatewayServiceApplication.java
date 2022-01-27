package com.justpickup.customerapigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CustomerApigatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApigatewayServiceApplication.class, args);
	}

}
