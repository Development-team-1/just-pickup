package com.justpickup.ownerfrontendservice.domain.order.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class OrderController {

    @GetMapping("/order")
    public String order() {
        return "/domain/order/order";
    }
}
