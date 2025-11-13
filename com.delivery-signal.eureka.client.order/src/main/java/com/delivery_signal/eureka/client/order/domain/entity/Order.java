package com.delivery_signal.eureka.client.order.domain.entity;

import com.delivery_signal.eureka.client.order.domain.exception.InvalidOrderStateException;
import com.delivery_signal.eureka.client.order.domain.vo.OrderStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    // 감사 필드
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

    public Order(String requestNote) {
        this.requestNote = requestNote;
    }

    @Builder
    public Order(UUID supplierCompanyId,
                 UUID receiverCompanyId,
                 UUID departureHubId,
                 UUID arrivalHubId,
                 String requestNote,
                 BigDecimal totalPriceAtOrder,
                 List<OrderProduct> orderProducts,
                 UUID deliveryId) {
        this.supplierCompanyId = supplierCompanyId;
        this.receiverCompanyId = receiverCompanyId;
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
        this.requestNote = requestNote;
        this.totalPriceAtOrder = totalPriceAtOrder;
        this.orderProducts = orderProducts;
        this.deliveryId = deliveryId;
        this.status = OrderStatus.VALID;
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

    public void markAsDeleted(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void cancel() {
        if (this.status == OrderStatus.CANCELED) {
            throw new InvalidOrderStateException("이미 취소된 주문입니다.");
        }
        if (this.status == OrderStatus.DELIVERED) {
            throw new InvalidOrderStateException("배송 완료된 주문은 취소할 수 없습니다.");
        }
        this.status = OrderStatus.CANCELED;
    }

    /**
     * 취소 여부 확인
     */
    public boolean isCanceled() {
        return this.status == OrderStatus.CANCELED
                || this.status == OrderStatus.INVALID;
    }
}
