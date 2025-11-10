package com.delivery_signal.eureka.client.delivery.domain.model;

import com.delivery_signal.eureka.client.delivery.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "p_delivery_route_records")
@SQLRestriction("deleted_at is NULL")
public class DeliveryRouteRecords extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "route_id", nullable = false, updatable = false)
    private UUID routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    // 배송 경로 상 허브의 순번
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    // 출발 허브 ID (허브 서비스와 연동)
    @Column(name = "from_hub_id", nullable = false)
    private UUID departureHubId;

    // 목적지 허브 ID
    @Column(name = "to_hub_id", nullable = false)
    private UUID destinationHubId;

    // 예상 거리
    @Column(name = "est_distance")
    private Double estDistance;

    // 에상 소요시간
    @Column(name = "est_time")
    private Long estTime;

    // 실제 거리
    @Column(name = "actual_distance")
    private Double actualDistance;

    // 실제 소요시간
    @Column(name = "actual_time")
    private Long actualTime;

    // 배송 현재 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "curr_status", nullable = false)
    private DeliveryStatus currStatus;

    // 허브 배송 담당자 ID
    @Column(name = "hub_delivery_manager_id")
    private Long hubDeliveryManagerId;

    // Delivery 도메인 객체와 RouteSegmentCommand를 받아 최초 이력 엔티티 생성
    public static DeliveryRouteRecords initialCreate(Delivery delivery, Integer sequence,
        UUID departureHubId, UUID destinationHubId, Double estDistance, Long estTime,
        Long initialHubManagerId, Long creatorId) {
        return DeliveryRouteRecords.builder()
            .delivery(delivery)
            .sequence(sequence)
            .departureHubId(departureHubId)
            .destinationHubId(destinationHubId)
            .estDistance(estDistance)
            .estTime(estTime)
            .currStatus(DeliveryStatus.HUB_WAITING) // 초기 상태
            .hubDeliveryManagerId(initialHubManagerId)
            .createdBy(creatorId)
            .build();
    }

    public void recordMovement(DeliveryStatus newStatus, Double actualDistance,
        Long actualTime, Long updatorId) {
        // 이미 도착 완료된 경로 재기록 방지
        if (this.currStatus.equals(DeliveryStatus.HUB_ARRIVED)) {
            throw new IllegalStateException("이미 허브 도착 처리된 경로입니다.");
        }

        // 상태 전이 규칙 (HUB_WAITING -> HUB_MOVING -> HUB_ARRIVED)
        // 다음 단계로의 전이만 가능
        if (this.currStatus.equals(DeliveryStatus.HUB_WAITING) && !newStatus.equals(DeliveryStatus.HUB_MOVING)) {
            throw new IllegalStateException("대기 중 상태에서는 이동 중 상태로만 변경 가능합니다.");
        }

        // 상태 역행 방지 로직 (예: HUB_MOVING -> HUB_WAITING 불가)
        if (newStatus.ordinal() < this.currStatus.ordinal()) {
            throw new IllegalStateException("상태를 이전 단계로 되돌릴 수 없습니다.");
        }

        this.currStatus = newStatus;
        this.actualDistance = actualDistance;
        this.actualTime = actualTime;
        super.update(updatorId);
    }
}
