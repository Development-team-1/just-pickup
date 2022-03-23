package com.justpickup.orderservice.domain.order.web;

import com.justpickup.orderservice.domain.order.dto.OrderDetailDto;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.exception.OrderException;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.global.client.store.OptionType;
import com.justpickup.orderservice.global.dto.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PatchMapping("/order/{orderId}")
    public ResponseEntity<Result> patchOrder(@PathVariable("orderId") Long orderId,
                                             @RequestBody PatchOrderRequest patchOrderRequest) {
        OrderStatus orderStatus = patchOrderRequest.getOrderStatus();
        if (orderStatus == OrderStatus.PENDING || orderStatus == OrderStatus.FAILED) {
            throw new OrderException(orderStatus.getMessage() + "는 변경 불가능합니다.");
        }

        orderService.modifyOrder(orderId, orderStatus);

        return ResponseEntity.ok(Result.createSuccessResult(null));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class PatchOrderRequest {
        private OrderStatus orderStatus;
    }

    @GetMapping("/api/order-detail/{orderId}")
    public ResponseEntity<Result> getOrderDetail(@PathVariable Long orderId) {

        OrderDetailDto orderDetail = orderService.findOrderDetail(orderId);
        return ResponseEntity.ok(Result.createSuccessResult(new OrderDetailResponse(orderDetail)));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class OrderDetailResponse {
        private Long id;
        private OrderStatus orderStatus;
        private String orderTime;
        private Long orderPrice;
        private String storeName;
        private OrderDetailUserResponse user;
        private List<OrderDetailItemResponse> orderItems = new ArrayList<>();

        public OrderDetailResponse(OrderDetailDto dto) {
            this.id = dto.getId();
            this.orderStatus = dto.getOrderStatus();
            this.orderTime = dto.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            this.orderPrice = dto.getOrderPrice();
            this.storeName = dto.getStoreName();
            this.user = new OrderDetailUserResponse(dto.getUser());
            this.orderItems = dto.getOrderItems().stream()
                    .map(OrderDetailItemResponse::new)
                    .collect(Collectors.toList());
        }

        @Data
        static class OrderDetailUserResponse {
            private Long id;
            private String name;
            private String phoneNumber;

            public OrderDetailUserResponse(OrderDetailDto.OrderDetailUser user) {
                this.id = user.getId();
                this.name = user.getName();
                this.phoneNumber = user.getPhoneNumber();
            }
        }

        @Data @NoArgsConstructor
        static class OrderDetailItemResponse {
            private Long id;
            private Long itemId;
            private long totalPrice;
            private long count;
            private String name;
            private List<OrderDetailItemOptionResponse> options = new ArrayList<>();

            public OrderDetailItemResponse(OrderDetailDto.OrderDetailItem orderDetailItem) {
                this.id = orderDetailItem.getId();
                this.itemId = orderDetailItem.getItemId();
                this.totalPrice = orderDetailItem.getTotalPrice();
                this.count = orderDetailItem.getCount();
                this.name = orderDetailItem.getName();
                this.options = orderDetailItem.getOptions().stream()
                        .map(OrderDetailItemOptionResponse::new)
                        .collect(Collectors.toList());
            }
        }

        @Data @NoArgsConstructor
        static class OrderDetailItemOptionResponse {
            private Long id;
            private Long itemOptionId;
            private String name;
            private OptionType optionType;

            public OrderDetailItemOptionResponse(OrderDetailDto.OrderDetailItemOption itemOption) {
                this.id = itemOption.getId();
                this.itemOptionId = itemOption.getItemOptionId();
                this.name = itemOption.getName();
                this.optionType = itemOption.getOptionType();
            }
        }
    }
}
