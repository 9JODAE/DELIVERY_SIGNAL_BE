package com.delivery_signal.eureka.client.company.domain.entity;


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

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void setUpdatedBy(Long userId){
        this.updatedBy = userId;
    }

    public void delete(Long userId){
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }

}
