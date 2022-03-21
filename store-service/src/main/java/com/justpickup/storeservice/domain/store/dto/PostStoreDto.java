package com.justpickup.storeservice.domain.store.dto;

import com.justpickup.storeservice.domain.map.entity.Map;
import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.global.entity.Address;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostStoreDto {
    private String name;
    private String phoneNumber;
    private Long userId;
    private _PostStoreAddress address;
    private _PostStoreMap map;

    @Builder
    public PostStoreDto(String name, String phoneNumber, Long userId, _PostStoreAddress address, _PostStoreMap map) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.address = address;
        this.map = map;
    }

    public static PostStoreDto of(Store store) {
        PostStoreDto postStoreDto = new PostStoreDto();
        postStoreDto.name = store.getName();
        postStoreDto.phoneNumber = store.getPhoneNumber();
        postStoreDto.userId = store.getUserId();
        postStoreDto.address = _PostStoreAddress.of(store.getAddress());
        postStoreDto.map = _PostStoreMap.of(store.getMap());
        return  postStoreDto;
    }

    @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class _PostStoreAddress {
        private String address;
        private String zipcode;

        @Builder
        public _PostStoreAddress(String address, String zipcode) {
            this.address = address;
            this.zipcode = zipcode;
        }

        public static _PostStoreAddress of(Address address) {
            _PostStoreAddress postStoreAddress = new _PostStoreAddress();
            postStoreAddress.address = address.getAddress();
            postStoreAddress.zipcode = address.getAddress();
            return postStoreAddress;
        }
    }

    @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class _PostStoreMap {
        private Double latitude;
        private Double longitude;

        @Builder
        public _PostStoreMap(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public static _PostStoreMap of(Map map) {
            _PostStoreMap postStoreMap = new _PostStoreMap();
            postStoreMap.latitude = map.getLatitude();
            postStoreMap.longitude = map.getLongitude();
            return postStoreMap;
        }
    }
}
