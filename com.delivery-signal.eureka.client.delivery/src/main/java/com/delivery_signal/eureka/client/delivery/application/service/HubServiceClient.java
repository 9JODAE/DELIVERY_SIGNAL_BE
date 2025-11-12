package com.delivery_signal.eureka.client.delivery.application.service;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hub-service", path = "${internal.hub.url}")
public interface HubServiceClient {
    String USER_ID_HEADER = "X-User-Id";
    String USER_ROLE_HEADER = "X-User-Role";

    /**
     * 허브의 존재 여부 확인 API
     * @param hubId 확인할 허브 ID
     * @param currUserId 현재 요청 사용자 ID (인가/감사 목적)
     * @param role 현재 요청 사용자 역할 (인가 목적)
     */
    @GetMapping("/open-api/v1/hubs/{hubId}")
    boolean existsById(
        @PathVariable("hubId") UUID hubId,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role);

    /**
     * 허브 이동 정보 목록 조회 (존재 여부 확인 및 정보 획득)
     * 요구사항: 주문 요청 들어올 시, 배송과 배송 경로 기록이 생성되어야 함
     * (배송 경로 기록 : 배송 경로는 최초에 모든 경로가 생성되어야 함)
     */

    // TODO: 추후 수정 예정
//    @GetMapping("/api/hub-routes/{order-id}")
//    List<RouteSegmentResponse> getHubRoutes(@PathVariable Long orderId);

    // (HubRoute Service의 경로 계산 API 명세 기반)
//    public record RouteSegmentResponse(
//        Integer sequence,
//        Long departureHubId,
//        Long arrivalHubId,
//        Double expectedDistance, // 예상 거리
//        Long expectedDuration // 예상 소요 시간 (초)
//    ) {}
}