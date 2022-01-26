package com.justpickup.discountservice.domain.event;

import com.justpickup.discountservice.global.entity.BaseEntity;
import com.justpickup.discountservice.global.entity.Photo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Clob;

@Table(name = "event")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    @Lob
    @Column(name ="content" )
    private String content;

    @Embedded
    private Photo photo;
}
