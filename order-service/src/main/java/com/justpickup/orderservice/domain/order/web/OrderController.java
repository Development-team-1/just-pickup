package com.justpickup.orderservice.domain.order.web;

import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.exception.OrderException;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.global.dto.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PatchMapping("/order/{orderId}")
    public ResponseEntity<Result> patchOrder(@PathVariable("orderId") Long orderId,
                                             @RequestBody PatchOrderRequest patchOrderRequest) {
        OrderStatus orderStatus = patchOrderRequest.getOrderStatus();
        if (orderStatus != OrderStatus.PLACED && orderStatus != OrderStatus.REJECTED) {
            throw new OrderException("주문 수락, 거절 외에는 변경 불가능합니다.");
        }

        orderService.modifyOrder(orderId, orderStatus);

        return ResponseEntity.ok(Result.createSuccessResult(null));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class PatchOrderRequest {
        private OrderStatus orderStatus;
    }
}
