package com.justpickup.orderservice.domain.order.entity;
import lombok.Getter;

// 주문 대기 -> 주문 신청  --> 주문수락 -> 픽업대기 -> 픽업완료
//                        \
//                         ㄴ> 주문거절
@Getter
public enum OrderStatus {
    PENDING("주문대기(장바구니)"),
    PLACED("주문신청"),
    ACCEPTED("주문수락"),
    REJECTED("주문거절"),
    WAITING("픽업대기"),
    FINISHED("픽업완료"),
    FAILED("실패");

    private String message;

    OrderStatus(String message) {
        this.message = message;
    }
}
