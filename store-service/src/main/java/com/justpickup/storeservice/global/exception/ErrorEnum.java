package com.justpickup.storeservice.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorEnum {

    NOT_EXIST_ITEM(HttpStatus.CONFLICT, "존재하지 않은 상품입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
