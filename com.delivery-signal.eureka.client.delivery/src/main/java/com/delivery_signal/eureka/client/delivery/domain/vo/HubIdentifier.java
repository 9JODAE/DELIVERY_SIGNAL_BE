package com.delivery_signal.eureka.client.delivery.domain.vo;

import java.util.UUID;

/**
 * Hub에서 전달받는 허브 ID를 나타내는 VO
 */
public record HubIdentifier(
    UUID hubId
) {
    // 도메인 유효성 검사 로직 추가
    public HubIdentifier {
        if (hubId == null) {
            throw new IllegalArgumentException("Hub ID는 null일 수 없습니다.");
        }
    }

    public static HubIdentifier of(UUID hubId) {
        return new HubIdentifier(hubId);
    }
}