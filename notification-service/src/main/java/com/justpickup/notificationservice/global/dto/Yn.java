package com.justpickup.notificationservice.global.dto;

import lombok.Getter;

@Getter
public enum Yn {
    Y(true), N(false);

    private boolean y;

    Yn(boolean y) {
        this.y = y;
    }
}
