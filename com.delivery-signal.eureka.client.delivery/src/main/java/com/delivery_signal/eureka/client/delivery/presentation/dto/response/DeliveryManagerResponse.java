package com.delivery_signal.eureka.client.delivery.presentation.dto.response;

import com.delivery_signal.eureka.client.delivery.application.dto.ManagerQueryResponse;
import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManagerType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

/**
 * 최종적으로 클라이언트에게 반환되는 응답 DTO (Presentation Layer)
 */
@Builder
public record DeliveryManagerResponse(
    Long managerId,
    UUID hubId,
    String slackId,
    DeliveryManagerType managerType,
    Integer deliverySequence,
    LocalDateTime createdAt
) {
}
