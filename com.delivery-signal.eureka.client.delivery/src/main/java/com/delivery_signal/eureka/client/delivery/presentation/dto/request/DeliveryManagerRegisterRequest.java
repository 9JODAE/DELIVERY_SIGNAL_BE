package com.delivery_signal.eureka.client.delivery.presentation.dto.request;


import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryManagerType;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeliveryManagerRegisterRequest(
    @NotNull(message = "사용자 ID는 필수입니다.")
    Long managerId, // UserService의 (user_id)

    @NotNull(message = "슬랙 ID는 필수입니다.")
    String slackId,

    @NotNull(message = "배송 담당자 타입은 필수입니다.")
    DeliveryManagerType type,

    // 업체 배송 담당자일 경우 필수. 허브 배송 담당자는 null 허용
    UUID hubId
) {


}
