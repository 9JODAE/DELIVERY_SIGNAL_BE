package com.delivery_signal.eureka.client.delivery.application.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hub-service", path = "/v1/hubs")
public interface HubServiceClient {

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
