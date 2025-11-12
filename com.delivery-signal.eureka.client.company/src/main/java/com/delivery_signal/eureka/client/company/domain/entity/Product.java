package com.delivery_signal.eureka.client.company.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.java.Log;

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
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "상품가격")
    private BigDecimal price;



    @Column(name = "deleted_by")
    private Long deletedBy;

    public void updateInfo(String productName, BigDecimal price, Long userId) {
        this.productName = productName;
        this.price = price;
        this.setUpdatedBy(userId);
    }

    public void softDelete(Long userId){
        if (this.isDeleted()) {
            return;
        }
        this.delete(userId);
    }
}
