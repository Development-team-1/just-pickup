package com.justpickup.userservice.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorEnum {

    NOT_EXIST_USER(HttpStatus.CONFLICT, "존재하지 않은 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
