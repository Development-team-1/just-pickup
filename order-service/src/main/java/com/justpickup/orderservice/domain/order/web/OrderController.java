package com.justpickup.orderservice.domain.order.web;

import com.justpickup.orderservice.domain.order.dto.OrderDto;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final PrevOrderSearchValidator prevOrderSearchValidator;

    @GetMapping("/orderMain")
    public ResponseEntity<Result> orderMain(@Valid OrderSearchCondition condition) {
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

    @GetMapping("/prevOrder")
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
            orders = orderDtoList.stream().map(OrderVo::new).collect(Collectors.toList());
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
                        .stream().map(OrderItemVo::new).collect(Collectors.toList());
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
