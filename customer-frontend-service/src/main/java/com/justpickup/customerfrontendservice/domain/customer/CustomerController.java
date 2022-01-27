package com.justpickup.customerfrontendservice.domain.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/")
public class CustomerController {

    @GetMapping("/food-home")
    public String hello(){

        return "/domain/food/food-home-1";
    }
}
