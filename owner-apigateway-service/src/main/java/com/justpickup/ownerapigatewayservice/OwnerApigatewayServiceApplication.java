package com.justpickup.ownerapigatewayservice;

import com.justpickup.ownerapigatewayservice.handler.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class OwnerApigatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwnerApigatewayServiceApplication.class, args);
	}

	@Bean
	public ErrorWebExceptionHandler globalExceptionHandler() {
		return new GlobalExceptionHandler();
	}

}
