package com.justpickup.notificationservice.domain.notification.entity;

import com.justpickup.notificationservice.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    /**
     * userdb.users.user_id
     */
    private Long userId;

}
