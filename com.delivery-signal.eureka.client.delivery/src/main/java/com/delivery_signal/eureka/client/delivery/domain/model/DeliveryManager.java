package com.delivery_signal.eureka.client.delivery.domain.model;

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
@Table(name = "p_delivery_managers")
@SQLRestriction("deleted_at is NULL")
public class DeliveryManager extends BaseEntity {

    /**
     * DeliveryManager의 ID는 User 엔티티의 ID와 동일
     * UserService의 User ID를 외래 키로 사용함
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id", nullable = false, updatable = false)
    private Long managerId;

    // 소속 허브 ID (업체 배송 담당자일 경우 필수, 허브 배송 담당자는 NULL 허용)
    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "slack_id", nullable = false)
    private String slackId;

    // 배송 담당자 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "manager_type", nullable = false)
    private DeliveryManagerType managerType;

    // 배송 순번 (배송 담당자 추가/삭제될 때에만 변경)
    @Column(name = "sequence", nullable = false)
    private Integer deliverySequence;

    public DeliveryManager(Long managerId, UUID hubId, String slackId, DeliveryManagerType managerType, Integer deliverySequence) {
        this.managerId = managerId;
        this.hubId = hubId;
        this.slackId = slackId;
        this.managerType = managerType;
        this.deliverySequence = deliverySequence;
    }

    public static DeliveryManager create(Long managerId, UUID hubId, String slackId, DeliveryManagerType managerType, Integer newSequence) {
        if (newSequence < 0) {
            throw new IllegalArgumentException("배송 순번은 0 이상이어야 합니다.");
        }
        return new DeliveryManager(managerId, hubId, slackId, managerType, newSequence);
    }

    public void update(UUID hubId, String slackId, DeliveryManagerType managerType) {
        this.hubId = hubId;
        this.slackId = slackId;
        this.managerType = managerType;
    }

    public void softDelete(Long userId) {
        super.markDeleted(userId);
    }
}
