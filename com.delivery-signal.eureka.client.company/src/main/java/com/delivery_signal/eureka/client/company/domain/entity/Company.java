package com.delivery_signal.eureka.client.company.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @Column(name = "company_id", nullable = false, updatable = false)
    private UUID companyId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", nullable = false)
    private CompanyType companyType;

    @Column(name = "company_address")
    private String address;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;


    public void updateInfo(String companyName, String address, CompanyType type, UUID hubId) {
        this.companyName = companyName;
        this.address = address;
        this.companyType = type;
        this.hubId = hubId;
        this.updatedAt = LocalDateTime.now();
        // updatedBy는 service 쪽에서 주입받거나 auditor로 처리
    }

    public void markAsDeleted(LocalDateTime now) {
        deletedAt = LocalDateTime.now();
    }
}
