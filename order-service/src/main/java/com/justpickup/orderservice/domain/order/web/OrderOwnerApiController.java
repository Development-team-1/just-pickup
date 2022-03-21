package com.justpickup.orderservice.domain.order.web;

import com.justpickup.orderservice.domain.order.dto.*;
import com.justpickup.orderservice.domain.order.entity.OrderStatus;
import com.justpickup.orderservice.domain.order.service.OrderService;
import com.justpickup.orderservice.domain.order.validator.PrevOrderSearchValidator;
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
import org.springframework.web.bind.annotation.RequestHeader;
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

    @GetMapping("/dashboard")
    public ResponseEntity<Result> dashboard( @RequestHeader(value="user-id") String userId) {

        DashBoardDto dashboardDto = orderService.findDashboard(Long.valueOf(userId));

        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(dashboardDto));
    }

    @GetMapping("/order-main")
    public ResponseEntity<Result> orderMain(@Valid OrderSearchCondition condition,
                                            @RequestHeader(value="user-id") String userHeader) {
        Long userId = Long.valueOf(userHeader);

        OrderMainDto orderMainDto = orderService.findOrderMain(condition, userId);

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
                                                @RequestHeader(value="user-id") String userHeader,
                                                BindingResult bindingResult) throws BindException {
        // validation
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);
        prevOrderSearchValidator.validate(prevOrderSearch, bindingResult);
        if (bindingResult.hasErrors()) throw new BindException(bindingResult);

        // get data
        Long userId = Long.valueOf(userHeader);
        Page<PrevOrderDto> prevOrderMain = orderService.findPrevOrderMain(prevOrderSearch, pageable, userId);

        // format data
        ResponsePrevOrder responsePrevOrder =
                new ResponsePrevOrder(prevOrderMain.getContent(), prevOrderMain.getNumber(), prevOrderMain.getTotalPages());
        return ResponseEntity.ok(Result.createSuccessResult(responsePrevOrder));
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    static class ResponsePrevOrder {
        private List<_Order> orders;
        private Page page;

        public ResponsePrevOrder(List<PrevOrderDto> orderDtoList, int startPage, int totalPage) {
            orders = orderDtoList.stream().map(_Order::new).collect(toList());
            page = new Page(startPage, totalPage);
        }

        @Data
        static class _Order {
            private Long orderId;
            private OrderStatus orderStatus;
            private String orderTime;
            private Long orderPrice;
            private String userName;
            private List<_OrderItem> orderItems;

            public _Order(PrevOrderDto prevOrderDto) {
                this.orderId = prevOrderDto.getId();
                this.orderStatus = prevOrderDto.getOrderStatus();
                this.orderTime = prevOrderDto.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                this.orderPrice = prevOrderDto.getOrderPrice();
                this.userName = prevOrderDto.getUserName();
                this.orderItems = prevOrderDto.getOrderItems()
                        .stream().map(_OrderItem::new).collect(toList());
            }
        }

        @Data
        static class _OrderItem {
            private Long orderItemId;
            private String orderItemName;

            public _OrderItem(PrevOrderDto._PrevOrderItem orderItemDto) {
                this.orderItemId = orderItemDto.getId();
                this.orderItemName = orderItemDto.getName();
            }
        }

        @Data @AllArgsConstructor
        static class Page {
            int startPage;
            int totalPage;
        }
    }
}
