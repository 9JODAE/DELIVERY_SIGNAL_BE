package com.delivery_signal.eureka.client.company.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "상품가격")
    private BigDecimal price;

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

    public void updateInfo(String productName, BigDecimal price) {
        this.productName = productName;
        this.price = price;
        this.updatedAt = LocalDateTime.now();
        // updatedBy는 service 쪽에서 주입받거나 auditor로 처리
    }

    public void markAsDeleted(LocalDateTime now) {
        deletedAt = LocalDateTime.now();
    }
}
