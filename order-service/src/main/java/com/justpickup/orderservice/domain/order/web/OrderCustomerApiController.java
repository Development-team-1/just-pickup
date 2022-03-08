package com.justpickup.orderservice.domain.order.web;

import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
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

        SliceImpl<OrderDto> orderHistory = orderService.findOrderHistory(pageable, userId);

        OrderHistoryResponse orderHistoryResponse =
                new OrderHistoryResponse(orderHistory.getContent(), orderHistory.hasNext());

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(orderHistoryResponse));
    }

    @Data @NoArgsConstructor
    static class OrderHistoryResponse {
        private List<_Order> orders;
        private boolean hasNext;

        public OrderHistoryResponse(List<OrderDto> orders, boolean hasNext) {
            this.orders = orders.stream().map(_Order::new).collect(Collectors.toList());
            this.hasNext = hasNext;
        }

        @Data
        static class _Order {
            private Long orderId;
            private String orderTime;
            private OrderStatus orderStatus;
            private String storeName;
            private Long orderPrice;
            private List<_OrderItem> orderItems;

            public _Order(OrderDto orderDto) {
                this.orderId = orderDto.getId();
                this.orderTime = orderDto.getOrderTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                this.orderStatus = orderDto.getOrderStatus();
                this.storeName = orderDto.getStoreId().toString();
                this.orderPrice = orderDto.getOrderPrice();
                this.orderItems = orderDto.getOrderItemDtoList()
                        .stream()
                        .map(_OrderItem::new)
                        .collect(Collectors.toList());
            }
        }

        @Data
        static class _OrderItem {
            private Long orderItemId;
            private String orderItemName;

            public _OrderItem(OrderItemDto orderItemDto) {
                this.orderItemId = orderItemDto.getItemId();
                this.orderItemName = orderItemDto.getItemId().toString();
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
                requestItem.itemId,
                requestItem.getPrice(),
                requestItem.getCount(),
                requestItem.getItemOptionIds());
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




}
