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
    private UUID createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private UUID updatedBy;

    private LocalDateTime deletedAt;
    private UUID deletedBy;

    public static OrderProduct create(Order order, UUID productId, String name, BigDecimal price, Integer qty) {
        /**
         * 외부호출에서 들어오는 값에 대한 예외처리, 아직 연결 전 이므로 주석처리
         */
//        if (productId == null) throw new IllegalArgumentException("productId required");
//        if (name == null || name.isBlank()) throw new IllegalArgumentException("productName required");
//        if (price == null || price.signum() < 0) throw new IllegalArgumentException("invalid price");

        OrderProduct p = new OrderProduct();
        p.order = order;
        p.productId = productId;
        p.productName = name;
        p.productPriceAtOrder = price;
        p.transferQuantity = qty;
        return p;
    }

}

