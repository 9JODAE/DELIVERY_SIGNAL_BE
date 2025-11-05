package com.delivery_signal.eureka.client.delivery.application.command;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManagerType;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryManagerRegisterRequest;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UpdateManagerCommand(
    Long managerId, // UserService의 (user_id)
    String slackId,
    DeliveryManagerType type,
    // 업체 배송 담당자일 경우 필수. 허브 배송 담당자는 null 허용
    UUID hubId
) {

    public static UpdateManagerCommand from(DeliveryManagerRegisterRequest request) {
        return UpdateManagerCommand.builder()
            .managerId(request.managerId())
            .slackId(request.slackId())
            .type(request.type())
            .hubId(request.hubId())
            .build();
    }
}
