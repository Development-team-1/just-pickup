package com.justpickup.storeservice.domain.review.entity;

import com.justpickup.storeservice.domain.reviewreply.entity.ReviewReply;
import com.justpickup.storeservice.domain.store.entity.Store;
import com.justpickup.storeservice.global.entity.BaseEntity;
import com.justpickup.storeservice.global.entity.Photo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "review")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    private String content;

    private Integer starRating;

    @Embedded
    private Photo photo;

    //== user-service.user pk ==//
    private Long userId;

    //== order-service.order pk ==//
    private Long orderId;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "review_reply_id")
    private ReviewReply reviewReply;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
