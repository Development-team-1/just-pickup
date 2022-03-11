package com.justpickup.orderservice.domain.order.entity;

public enum OrderStatus {
    PENDING("주문대기"),
    ORDER("주문"),
    PLACED("주문수락"),
    REJECT("주문거절"),
    FAIL("주문실패");

    private String message;

    OrderStatus(String message) {
        this.message = message;
    }
}
