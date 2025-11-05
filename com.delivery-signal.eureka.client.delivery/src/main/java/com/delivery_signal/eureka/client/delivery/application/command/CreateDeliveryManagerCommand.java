package com.delivery_signal.eureka.client.delivery.application.command;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManagerType;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryManagerRegisterRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

/**
 * 배송 담당자 등록
 */
@Builder
// TODO: 루트 경로는 추후에 추가 예정
public record CreateDeliveryManagerCommand(
    @NotNull(message = "사용자 ID는 필수입니다.")
    Long managerId, // UserService의 (user_id)

    @NotNull(message = "슬랙 ID는 필수입니다.")
    String slackId,

    @NotNull(message = "배송 담당자 타입은 필수입니다.")
    DeliveryManagerType type,

    // 업체 배송 담당자일 경우 필수. 허브 배송 담당자는 null 허용
    UUID hubId
) {

    public static CreateDeliveryManagerCommand from(DeliveryManagerRegisterRequest request) {
        return CreateDeliveryManagerCommand.builder()
            .managerId(request.managerId())
            .slackId(request.slackId())
            .type(request.type())
            .hubId(request.hubId())
            .build();
    }
}
