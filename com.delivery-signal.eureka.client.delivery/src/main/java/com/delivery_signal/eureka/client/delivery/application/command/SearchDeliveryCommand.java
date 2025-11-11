package com.delivery_signal.eureka.client.delivery.application.command;

import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryStatus;
import java.util.UUID;

public record SearchDeliveryCommand(
    UUID hubId, // 허브 ID
    UUID companyId, // 업체 ID
    Long deliveryManagerId, // 배송 담당자 ID
    DeliveryStatus status // 배송 상태
) {

    public static SearchDeliveryCommand of(DeliveryStatus status, UUID hubId, UUID companyId,
        Long deliveryManagerId) {
        return new SearchDeliveryCommand(hubId, companyId, deliveryManagerId, status);
    }
}
