package com.delivery_signal.eureka.client.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_order_products")
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class) //생성, 수정 자동
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderProduct{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_product_id")
    private UUID orderProductId;

    // N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private BigDecimal productPriceAtOrder;

    @Column(nullable = false)
    private Integer transferQuantity;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private Long createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private Long updatedBy;

    private LocalDateTime deletedAt;
    private Long deletedBy;

    // ===========================
    // 정적 팩토리 메서드
    // ===========================
    public static OrderProduct create(
            Order order,
            UUID productId,
            String productName,
            BigDecimal price,
            Integer quantity
    ) {

        if (order == null) throw new IllegalArgumentException("Order cannot be null");
        if (productId == null) throw new IllegalArgumentException("productId required");
        if (productName == null || productName.isBlank()) throw new IllegalArgumentException("productName required");
        if (price == null || price.signum() < 0) throw new IllegalArgumentException("Invalid product price");
        if (quantity == null || quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        OrderProduct p = new OrderProduct();
        p.order = order;
        p.productId = productId;
        p.productName = productName;
        p.productPriceAtOrder = price;
        p.transferQuantity = quantity;

        return p;
    }

    public void updateQuantity(Integer transferQuantity) {
        if (transferQuantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
        this.transferQuantity = transferQuantity;
    }

    public void markAsDeleted(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}

