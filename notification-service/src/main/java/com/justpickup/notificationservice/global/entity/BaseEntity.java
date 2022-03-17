package com.justpickup.notificationservice.global.entity;


import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @CreationTimestamp
    private LocalDateTime createdAt;
    private Long createdBy;
    @UpdateTimestamp
    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;

}
