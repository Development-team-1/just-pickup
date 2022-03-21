package com.justpickup.userservice.domain.user.dto;

import com.justpickup.userservice.global.client.store.PostStoreRequest;
import lombok.Builder;
import lombok.Getter;


@Getter
public class PostStoreDto {
    private String name;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private Double latitude;
    private Double longitude;

    @Builder
    public PostStoreDto(String name, String phoneNumber, String address, String zipcode, Double latitude, Double longitude) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PostStoreRequest toPostStoreRequest() {
        return PostStoreRequest.builder()
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .address(this.address)
                .zipcode(this.zipcode)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }
}
