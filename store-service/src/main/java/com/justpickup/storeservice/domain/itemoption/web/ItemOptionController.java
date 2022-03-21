package com.justpickup.storeservice.domain.itemoption.web;

import com.justpickup.storeservice.domain.itemoption.service.ItemOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemOptionController {

    private final ItemOptionService itemOptionService;
}
