package com.justpickup.orderservice.domain.order.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data @NoArgsConstructor @AllArgsConstructor
public class OrderSearchCondition {

    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$",
            message = "YYYY-MM-DD 형식에 맞게 작성되지 않았습니다.")
    private String orderDate;
    @Min(value = 1,
            message = "마지막 주문 번호는 1보다 작을 수 없습니다.")
    private Long lastOrderId;

    public LocalDateTime getOrderStartTime() {
        LocalDate orderTime = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return orderTime.atStartOfDay();
    }

    public LocalDateTime getOrderEndTime() {
        LocalDate orderTime = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return LocalDateTime.of(orderTime, LocalTime.of(23, 59, 59));
    }
}