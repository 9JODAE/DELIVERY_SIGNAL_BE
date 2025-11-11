package com.delivery_signal.eureka.client.delivery.application.dto;

import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryStatus;
import java.util.UUID;
import lombok.Builder;

@Builder
public record DeliverySearchCondition(
    UUID hubId, // 허브 ID
    UUID companyId, // 업체 ID
    Long deliveryManagerId, // 배송 담당자 ID
    DeliveryStatus status // 배송 상태
) {
    public static DeliverySearchCondition of(UUID hubId, Long deliveryManagerId, UUID companyId, DeliveryStatus status) {
        return DeliverySearchCondition.builder()
            .hubId(hubId)
            .deliveryManagerId(deliveryManagerId)
            .companyId(companyId)
            .status(status)
            .build();
    }
}
