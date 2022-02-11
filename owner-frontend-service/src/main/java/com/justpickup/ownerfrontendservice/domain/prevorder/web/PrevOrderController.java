package com.justpickup.ownerfrontendservice.domain.prevorder.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrevOrderController {

    @GetMapping("/prev-order")
    public String prevOrder() {
        return "domain/prev-order/prev-order";
    }
}
