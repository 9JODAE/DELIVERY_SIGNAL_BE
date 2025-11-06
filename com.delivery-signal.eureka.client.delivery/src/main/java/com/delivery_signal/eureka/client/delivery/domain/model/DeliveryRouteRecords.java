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
    private Double estTime;

    // 실제 거리
    @Column(name = "actual_distance")
    private Double actualDistance;

    // 실제 소요시간
    @Column(name = "actual_time")
    private Double actualTime;

    // 배송 현재 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "curr_status", nullable = false)
    private DeliveryStatus currStatus;

    // 허브 배송 담당자 ID
    @Column(name = "hub_delivery_manager_id", nullable = false)
    private Long hubDeliveryManagerId;
}
