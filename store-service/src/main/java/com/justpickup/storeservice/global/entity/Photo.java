package com.justpickup.storeservice.global.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Getter
public class Photo {

    @Column(name = "photo_path")
    private String path;
    @Column(name = "photo_name")
    private String name;
}
