package com.justpickup.orderservice.domain.orderItem.web;

import com.justpickup.orderservice.domain.orderItem.service.OrderItemService;
import com.justpickup.orderservice.global.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/orderItem")
public class OrderItemCustomerApiController {

    private final OrderItemService orderItemService;

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity deleteOrderItem(@PathVariable Long orderItemId,
                                          @RequestHeader(value = "user-id") String userId){
        orderItemService.deleteOrderItem(orderItemId,Long.parseLong(userId));

        return ResponseEntity.status(HttpStatus.OK).body(Result.createSuccessResult(orderItemId));
    }

}
