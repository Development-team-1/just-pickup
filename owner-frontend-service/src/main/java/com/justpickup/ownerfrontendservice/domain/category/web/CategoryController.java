package com.justpickup.ownerfrontendservice.domain.category.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

    @GetMapping("/category")
    public String category(){

        return "/domain/category/category";
    }
}
