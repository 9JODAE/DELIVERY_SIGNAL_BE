package com.delivery_signal.eureka.client.order.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_orders")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID id;

    @Column
    private UUID deliveryId;

    @Column
    private UUID supplierCompanyId;

    @Column
    private UUID receiverCompanyId;

    @Column
    private UUID departureHubId;

    @Column
    private UUID arrivalHubId;

    @Column
    private String requestNote;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    // 연관관계 설정
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    // 감사 필드
    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private UUID createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private UUID updatedBy;

    private LocalDateTime deletedAt;
    private UUID deletedBy;

    public Order(UUID supplierCompanyId, UUID receiverCompanyId, String requestNote) {
        this.supplierCompanyId = supplierCompanyId;
        this.receiverCompanyId = receiverCompanyId;
        this.requestNote = requestNote;
    }

    // 도메인 메서드
    public void addOrderProducts(List<OrderProduct> products) {
        for (OrderProduct p : products) {
            this.addOrderProduct(p);
        }
    }

    public void addOrderProduct(OrderProduct product) {
        orderProducts.add(product);
    }
}
