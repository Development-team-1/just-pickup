package com.justpickup.userservice.global.client.store;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PostStoreRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String address;
    @NotEmpty
    private String zipcode;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    @Builder
    public PostStoreRequest(String name, String phoneNumber, String address, String zipcode, Double latitude, Double longitude) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
