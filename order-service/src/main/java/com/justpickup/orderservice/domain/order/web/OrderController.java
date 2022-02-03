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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orderMain")
    public ResponseEntity orderMain(@Valid OrderMainRequest orderMainRequest) {

        List<OrderDto> orderDto = orderService.findOrderMain(orderMainRequest.convertOrderTimeToLocalDate());

        List<OrderMainResponse> orderMainResponses = orderDto.stream()
                .map(OrderMainResponse::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result<>("OK", orderMainResponses));
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class OrderMainRequest {
        // yyyy-mm-dd 형태를 가지는 패턴 조사
        @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$",
                message = "YYYY-MM-DD 형식에 맞게 작성되지 않았습니다.")
        private String orderTime;

        public LocalDate convertOrderTimeToLocalDate() {
            return LocalDate.parse(orderTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
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
