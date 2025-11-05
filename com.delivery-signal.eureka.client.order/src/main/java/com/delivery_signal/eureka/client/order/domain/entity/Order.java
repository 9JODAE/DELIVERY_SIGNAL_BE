package com.delivery_signal.eureka.client.order.domain.entity;

import com.delivery_signal.eureka.client.order.domain.exception.InvalidOrderStateException;
import com.delivery_signal.eureka.client.order.presentation.dto.response.OrderUpdateResponseDto;
import jakarta.persistence.*;
import lombok.Builder;
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
    private BigDecimal totalPriceAtOrder;

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

    public Order(String requestNote) {
        this.requestNote = requestNote;
    }

    @Builder
    public Order(UUID supplierCompanyId,
                 UUID receiverCompanyId,
                 String requestNote,
                 BigDecimal totalPrice,
                 List<OrderProduct> orderProducts,
                 UUID deliveryId) {
        this.supplierCompanyId = supplierCompanyId;
        this.receiverCompanyId = receiverCompanyId;
        this.requestNote = requestNote;
        this.totalPriceAtOrder = totalPrice;
        this.orderProducts = orderProducts;
        this.deliveryId = deliveryId;
    }


    /**
     * 주문 요청사항 수정
     * @param requestNote 수정할 요청사항
     */
    public void updateRequestNote(String requestNote) {
        if (requestNote != null && requestNote.length() > 200) {
            throw new InvalidOrderStateException("요청사항은 200자를 초과할 수 없습니다.");
        }
        this.requestNote = requestNote;
    }
}
