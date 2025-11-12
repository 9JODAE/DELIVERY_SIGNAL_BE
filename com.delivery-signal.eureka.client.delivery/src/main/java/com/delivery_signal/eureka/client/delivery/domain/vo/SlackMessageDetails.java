package com.delivery_signal.eureka.client.delivery.domain.vo;

import lombok.Builder;

/**
 * 슬랙 메시지 발송에 필요한 정보를 담는 Value Object
 */
@Builder
public record SlackMessageDetails(
    String slackUserId,
    String orderNum,
    String orderUserInfo,
    String orderTime,
    String productInfo,
    String detailRequest,
    String origin,
    String layOver,
    String destination,
    String deliveryUserInfo
) {
}
