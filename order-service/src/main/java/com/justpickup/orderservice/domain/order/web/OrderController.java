package com.justpickup.orderservice.domain.order.web;

import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.dto.OrderSearchCondition;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.global.dto.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orderMain")
    public ResponseEntity orderMain(@Valid OrderSearchCondition condition) {
        // TODO: 2022/02/04 JWT 구현 시 변경 요망
        Long userId = 1L;
        Long storeId = 1L;

        List<OrderDto> orderDto = orderService.findOrderMain(condition, storeId);

        List<OrderMainResponse> orderMainResponses = orderDto.stream()
                .map(OrderMainResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(orderMainResponses));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class OrderMainResponse {
        private Long orderId;
        private Long userId;
        private String userName;
        private List<OrderItemResponse> orderItemResponses;
        private OrderStatus orderStatus;
        private String orderTime;
        
        public OrderMainResponse(OrderDto orderDto) {
            List<OrderItemResponse> orderItemDtoList = orderDto.getOrderItemDtoList()
                    .stream()
                    .map(OrderItemResponse::new)
                    .collect(Collectors.toList());

            this.orderId = orderDto.getId();
            this.userId = orderDto.getUserId();
            this.userName = orderDto.getUserName();
            this.orderItemResponses = orderItemDtoList;
            this.orderStatus = orderDto.getOrderStatus();
            this.orderTime = orderDto.getOrderTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    @Data
    static class OrderItemResponse {
        private Long orderItemId;
        private Long itemId;
        private String itemName;
        
        public OrderItemResponse(OrderItemDto orderItemDto) {
            this.orderItemId = orderItemDto.getId();
            this.itemId = orderItemDto.getItemId();
            this.itemName = orderItemDto.getItemName();
        }
    }
}
