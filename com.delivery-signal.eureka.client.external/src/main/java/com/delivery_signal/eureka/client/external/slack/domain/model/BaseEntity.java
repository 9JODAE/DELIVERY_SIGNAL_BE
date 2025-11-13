package com.delivery_signal.eureka.client.external.slack.domain.model;


import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@DynamicUpdate
@Access(AccessType.FIELD)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreatedBy
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;

    private Long deletedBy;

    public void createdBy(Long userId){
        this.createdBy = userId;
    }

    public void updatedBy(Long userId){
        this.updatedBy = userId;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void delete(Long userId){
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }

}
