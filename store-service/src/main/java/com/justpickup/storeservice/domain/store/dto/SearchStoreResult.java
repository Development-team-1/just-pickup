package com.justpickup.storeservice.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.DecimalFormat;

@Getter @AllArgsConstructor
public class SearchStoreResult {
    private Long storeId;
    private String storeName;
    private Double distanceMeter;

    public String convertDistanceToString() {
        // km 으로 표시
        if (distanceMeter >= 1000) {
            double km = distanceMeter * 0.001;
            String format = new DecimalFormat("0.0").format(km);
            // ex) 1.7km
            return format + "km";
        }

        // ex) 621m
        return new DecimalFormat("0").format(distanceMeter) + "m";
    }
}
