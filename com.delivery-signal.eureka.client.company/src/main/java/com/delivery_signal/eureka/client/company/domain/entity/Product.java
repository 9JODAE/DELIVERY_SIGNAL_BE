package com.delivery_signal.eureka.client.company.domain.entity;

import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
    private UUID id;


    @Column(name = "company_id")
    private UUID companyId;


    @Column(name = "product_name")
    private String name;


    @Column(name = "hub_id")
    private UUID hubId;


    @Column(name = "상품수량")
    private Integer quantity;


    @Column(name = "상품가격")
    private BigDecimal price;


    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;


    @Column(name = "deleted_by")
    private Long deletedBy;
}
