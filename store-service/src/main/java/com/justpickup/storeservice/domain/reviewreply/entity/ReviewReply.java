package com.justpickup.storeservice.domain.reviewreply.entity;

import com.justpickup.storeservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "review_reply")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewReply extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "review_reply_id")
    private Long id;

    private String content;
}
