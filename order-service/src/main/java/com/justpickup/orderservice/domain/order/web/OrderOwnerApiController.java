package com.justpickup.orderservice.domain.order.web;

import com.justpickup.orderservice.domain.order.dto.OrderDto;
import com.justpickup.orderservice.domain.order.dto.OrderMainDto;
import com.justpickup.orderservice.domain.order.dto.OrderSearchCondition;
import com.justpickup.orderservice.domain.order.dto.PrevOrderSearch;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.order.validator.PrevOrderSearchValidator;
import com.justpickup.orderservice.domain.orderItem.dto.OrderItemDto;
import com.justpickup.orderservice.global.dto.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/owner/order")
@RequiredArgsConstructor
@Slf4j
public class OrderOwnerApiController {

    private final OrderService orderService;
    private final PrevOrderSearchValidator prevOrderSearchValidator;

    @GetMapping("/order-main")
    public ResponseEntity<Result> orderMain(@Valid OrderSearchCondition condition) {
        // TODO: 2022/03/10 Feign client storeId 가져오기 구현 필요
        Long userId = 1L;
        Long storeId = 1L;

        OrderMainDto orderMainDto = orderService.findOrderMain(condition, storeId);

        OrderMainResponse orderMainResponse = new OrderMainResponse(orderMainDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(orderMainResponse));
    }

    @Data @NoArgsConstructor
    static class OrderMainResponse {
        private boolean hasNext;
        private List<_Order> orders;

        public OrderMainResponse(OrderMainDto orderMainDto) {
            this.hasNext = orderMainDto.isHasNext();
            this.orders = orderMainDto.getOrders()
                    .stream()
                    .map(_Order::new)
                    .collect(toList());
        }

        @Data
        static class _Order {
            private Long id;
            private String orderTime;
            private String orderStatus;
            private String userName;
            private String storeName;
            private List<_OrderItem> orderItems;

            public _Order(OrderMainDto._Order order) {
                this.id = order.getId();
                this.orderTime = order.getOrderTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                this.orderStatus = order.getOrderStatus().name();
                this.userName = order.getUserName();
                this.storeName = order.getStoreName();
                this.orderItems = order.getOrderItems()
                        .stream().map(_OrderItem::new).collect(toList());
            }
        }

        @Data
        static class _OrderItem {
            private String itemName;

            public _OrderItem(OrderMainDto._OrderItem orderItem) {
                this.itemName = orderItem.getItemName();
            }
        }
    }

    @GetMapping("/prev-order")
    public ResponseEntity<Result> findPrevOrder(@Valid PrevOrderSearch prevOrderSearch,
                                                @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                BindingResult bindingResult) throws BindException {
        // validation
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        prevOrderSearchValidator.validate(prevOrderSearch, bindingResult);
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);

        // get data
        Page<OrderDto> prevOrderMain = orderService.findPrevOrderMain(prevOrderSearch, pageable, 1L);

        // format data
        ResponsePrevOrder responsePrevOrder =
                new ResponsePrevOrder(prevOrderMain.getContent(), prevOrderMain.getNumber(), prevOrderMain.getTotalPages());
        return ResponseEntity.ok(Result.createSuccessResult(responsePrevOrder));
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    static class ResponsePrevOrder {
        private List<OrderVo> orders;
        private Page page;

        public ResponsePrevOrder(List<OrderDto> orderDtoList, int startPage, int totalPage) {
            orders = orderDtoList.stream().map(OrderVo::new).collect(toList());
            page = new Page(startPage, totalPage);
        }

        @Data
        static class OrderVo {
            private Long orderId;
            private OrderStatus orderStatus;
            private String orderTime;
            private Long orderPrice;
            private String userName;
            private List<OrderItemVo> orderItems;

            public OrderVo(OrderDto orderDto) {
                this.orderId = orderDto.getId();
                this.orderStatus = orderDto.getOrderStatus();
                this.orderTime = orderDto.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                this.orderPrice = orderDto.getOrderPrice();
                this.userName = orderDto.getUserName();
                this.orderItems = orderDto.getOrderItemDtoList()
                        .stream().map(OrderItemVo::new).collect(toList());
            }
        }

        @Data
        static class OrderItemVo {
            private Long orderItemId;
            private String orderItemName;

            public OrderItemVo(OrderItemDto orderItemDto) {
                this.orderItemId = orderItemDto.getId();
                this.orderItemName = orderItemDto.getItemName();
            }
        }

        @Data @AllArgsConstructor
        static class Page {
            int startPage;
            int totalPage;
        }
    }
}
