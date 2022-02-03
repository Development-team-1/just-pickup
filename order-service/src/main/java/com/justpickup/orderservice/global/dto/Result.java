package com.justpickup.orderservice.global.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Result<T> {
    private String message;
    private T data;
}
