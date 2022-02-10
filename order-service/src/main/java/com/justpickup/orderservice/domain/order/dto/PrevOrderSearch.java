package com.justpickup.orderservice.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class PrevOrderSearch {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "시작일은 필수 값입니다.")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "종료일은 필수 값입니다.")
    private LocalDate endDate;

    public LocalDateTime getStartDateTime() {
        return startDate.atStartOfDay();
    }

    public LocalDateTime getEndDateTime() {
        return LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
    }
}
