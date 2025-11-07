package com.delivery_signal.eureka.client.delivery.domain.model;

import com.delivery_signal.eureka.client.delivery.application.command.CreateDeliveryCommand;
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

    public Delivery(UUID deliveryId, UUID orderId, UUID companyId,
        DeliveryStatus currStatus, UUID departureHubId,
        UUID destinationHubId, String deliveryAddress, String recipient,
        String recipientSlackId, Long deliveryManagerId) {

        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.companyId = companyId;
        this.currStatus = currStatus;
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.deliveryAddress = deliveryAddress;
        this.recipient = recipient;
        this.recipientSlackId = recipientSlackId;
        this.deliveryManagerId = deliveryManagerId;
    }

    public static Delivery create(CreateDeliveryCommand command, Long creatorId) {
        return Delivery.builder()
            .orderId(command.orderId())
            .companyId(command.companyId())
            .currStatus(DeliveryStatus.valueOf(command.status()))
            .departureHubId(command.departureHubId())
            .destinationHubId(command.destinationHubId())
            .deliveryAddress(command.address())
            .recipient(command.recipient())
            .recipientSlackId(command.recipientSlackId())
            .deliveryManagerId(command.deliveryManagerId())
            .createdBy(creatorId)
            .build();
    }
}


