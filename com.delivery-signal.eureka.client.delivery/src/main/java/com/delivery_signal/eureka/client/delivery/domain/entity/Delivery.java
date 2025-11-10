package com.delivery_signal.eureka.client.delivery.domain.entity;

import com.delivery_signal.eureka.client.delivery.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_deliveries")
@SQLRestriction("deleted_at is NULL")
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id", nullable = false, updatable = false)
    private UUID deliveryId;

    // 주문 ID (Order Service와 연동)
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    // 업체 ID
    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    // 배송 현재 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "curr_status", nullable = false)
    private DeliveryStatus currStatus;

    // 출발 허브 ID (허브 서비스와 연동)
    @Column(name = "from_hub_id", nullable = false)
    private UUID departureHubId;

    // 목적지 허브 ID
    @Column(name = "to_hub_id", nullable = false)
    private UUID destinationHubId;

    // 배송지 주소
    @Column(name = "address", nullable = false)
    private String deliveryAddress;

    // 수령인
    @Column(name = "recipient", nullable = false)
    private String recipient;

    // 수령인 슬랙 ID / 전화번호
    @Column(name = "recipient_slack_id")
    private String recipientSlackId;

    // 업체 배송담당자 ID
    @Column(name = "delivery_manager_id", nullable = false)
    private Long deliveryManagerId;

    public static Delivery create(UUID orderId, UUID companyId, String status, UUID departureHubId,
        UUID destinationHubId, String address, String recipient,
        String recipientSlackId, Long deliveryManagerId, Long creatorId) {
        return Delivery.builder()
            .orderId(orderId)
            .companyId(companyId)
            .currStatus(DeliveryStatus.valueOf(status))
            .departureHubId(departureHubId)
            .destinationHubId(destinationHubId)
            .deliveryAddress(address)
            .recipient(recipient)
            .recipientSlackId(recipientSlackId)
            .deliveryManagerId(deliveryManagerId)
            .createdBy(creatorId)
            .build();
    }

    /**
     * 배송 상태 업데이트
     */
    public void updateStatus(DeliveryStatus newStatus, Long updatorId) {
        if (currStatus.equals(DeliveryStatus.DELIVERY_COMPLETED)) {
            throw new IllegalStateException("완료된 배송의 상태는 변경할 수 없습니다.");
        }

        // 최종 허브에서 업체 전달 완료(DELIVERY_COMPLETED)까지는 Delivery 엔티티의 상태로 확인
        if (this.currStatus.equals(DeliveryStatus.DELIVERING) &&
            !newStatus.equals(DeliveryStatus.DELIVERY_COMPLETED)) {
            throw new IllegalStateException("업체 이동 중 상태에서는 완료만 가능합니다.");
        }

        // 상태 역행 방지 로직 (예: DELIVERING -> HUB_WAITING 불가)
        if (newStatus.ordinal() < this.currStatus.ordinal()) {
            if (!this.currStatus.equals(DeliveryStatus.HUB_ARRIVED)) {
                throw new IllegalStateException("상태를 이전 단계로 되돌릴 수 없습니다.");
            }
        }

        this.currStatus = newStatus;
        super.update(updatorId);
    }

    /**
     * 논리적 삭제
     */
    public void softDelete(Long deleterId) {
        // 삭제 전 조건 확인 (요구사항: 배송이 시작된 상태는 삭제 불가)
        if (!this.currStatus.equals(DeliveryStatus.HUB_WAITING)) {
            throw new IllegalStateException("배송이 시작된(" + this.currStatus.name() + ") 상태에서는 삭제할 수 없습니다.");
        }
        super.markDeleted(deleterId);
    }
}


