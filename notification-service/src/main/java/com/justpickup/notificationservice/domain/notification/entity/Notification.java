package com.justpickup.notificationservice.domain.notification.entity;

import com.justpickup.notificationservice.global.dto.Yn;
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

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    /**
     * userdb.users.user_id
     */
    private Long userId;

    private String title;

    private String message;

    @Enumerated(EnumType.STRING)
    private Yn readYn;

    // == 생성 메소드 == //
    public static Notification of(Long userId, String message, String title) {
        Notification notification = new Notification();
        notification.userId = userId;
        notification.message = message;
        notification.title = title;
        notification.readYn = Yn.N;
        return notification;
    }

    public void modifyReadYn(Yn readYn) {
        this.readYn = readYn;
    }
}
