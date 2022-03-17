package com.justpickup.orderservice.domain.order.web;

import com.justpickup.orderservice.domain.order.dto.FetchOrderDto;
import com.justpickup.orderservice.domain.order.dto.OrderHistoryDto;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.domain.orderItemOption.dto.OrderItemOptionDto;
import com.justpickup.orderservice.global.dto.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer/order")
@RequiredArgsConstructor
@Slf4j
public class OrderCustomerApiController {

    private final OrderService orderService;

    @GetMapping("/history")
    public ResponseEntity<Result> getOrderHistory(@RequestHeader(value = "user-id") String userHeader,
                                                  @PageableDefault(page = 0, size = 3) Pageable pageable) {
        Long userId = Long.parseLong(userHeader);

        SliceImpl<OrderHistoryDto> orderHistory = orderService.findOrderHistory(pageable, userId);

        OrderHistoryResponse orderHistoryResponse =
                new OrderHistoryResponse(orderHistory.getContent(), orderHistory.hasNext());

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(orderHistoryResponse));
    }

    @Data @NoArgsConstructor
    static class OrderHistoryResponse {
        private List<_OrderResponse> orders;
        private boolean hasNext;

        public OrderHistoryResponse(List<OrderHistoryDto> orders, boolean hasNext) {
            this.orders = orders.stream().map(_OrderResponse::new).collect(Collectors.toList());
            this.hasNext = hasNext;
        }

        @Data
        static class _OrderResponse {
            private Long orderId;
            private String orderTime;
            private OrderStatus orderStatus;
            private Long storeId;
            private String storeName;
            private Long orderPrice;
            private List<_OrderItemResponse> orderItems;

            public _OrderResponse(OrderHistoryDto orderHistoryDto) {
                this.orderId = orderHistoryDto.getId();
                this.orderTime = orderHistoryDto.getOrderTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                this.orderStatus = orderHistoryDto.getOrderStatus();
                this.storeId = orderHistoryDto.getStoreId();
                this.storeName = orderHistoryDto.getStoreName();
                this.orderPrice = orderHistoryDto.getPrice();
                this.orderItems = orderHistoryDto.getOrderItems()
                        .stream()
                        .map(_OrderItemResponse::new)
                        .collect(Collectors.toList());
            }
        }

        @Data
        static class _OrderItemResponse {
            private Long orderItemId;
            private String orderItemName;

            public _OrderItemResponse(OrderHistoryDto._OrderHistoryItem orderHistoryItem) {
                this.orderItemId = orderHistoryItem.getItemId();
                this.orderItemName = orderHistoryItem.getItemName();
            }
        }
    }

    /**
     * order
     */

    @PostMapping("item")
    public ResponseEntity addItemToBasket( @RequestBody RequestItem requestItem,
                                           @RequestHeader(value = "user-id") String userId){
        OrderItemDto orderItemDto = OrderItemDto.of(-1L,
                requestItem.getItemId(),
                requestItem.getPrice(),
                requestItem.getCount(),
                requestItem.getItemOptionIds().stream().map(OrderItemOptionDto::new).collect(Collectors.toList()));
        orderService.addItemToBasket(orderItemDto,requestItem.getStoreId() ,Long.parseLong(userId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Result.createSuccessResult(null));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestItem{
        private Long itemId;
        private Long storeId;
        private Long price;
        private Long count;
        private List<Long> itemOptionIds ;
    }

    @GetMapping("/orders")
    public ResponseEntity fetchOrder(@RequestHeader(value = "user-id") String userId){
        FetchOrderDto fetchOrderDto = orderService.fetchOrder(Long.parseLong(userId));
        return ResponseEntity.ok(Result.createSuccessResult(fetchOrderDto));
    }

    @PostMapping("/orders")
    public ResponseEntity saveOrder(@RequestHeader(value = "user-id") String userId){
        orderService.saveOrder(Long.parseLong(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(Result.createSuccessResult(null));
    }

}
