package com.delivery_signal.eureka.client.delivery.application.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-service", path = "/open-api/v1")
public interface HubServiceClient {
    String USER_ID_HEADER = "X-User-Id";
    String USER_ROLE_HEADER = "X-User-Role";

    /**
     * 허브의 존재 여부 확인 API
     * @param hubId 확인할 허브 ID
     * @param currUserId 현재 요청 사용자 ID (인가/감사 목적)
     * @param role 현재 요청 사용자 역할 (인가 목적)
     */
    @GetMapping("/hubs/{hubId}")
    ApiResponse<Boolean> existsById(
        @PathVariable("hubId") UUID hubId,
        @RequestHeader(USER_ID_HEADER) Long currUserId,
        @RequestHeader(USER_ROLE_HEADER) String role);

    /**
     * 허브 이동 정보 목록 조회 (존재 여부 확인 및 정보 획득)
     * 요구사항: 주문 요청 들어올 시, 배송과 배송 경로 기록이 생성되어야 함
     * (배송 경로 기록 : 배송 경로는 최초에 모든 경로가 생성되어야 함)
     */
    @GetMapping("/routes")
    ApiResponse<List<PathResponse>> searchHubRoutes(
        @RequestParam("departure") UUID departureHubId,
        @RequestParam("arrival") UUID arrivalHubId
    );

    record PathResponse(
        UUID departureHubId,
        UUID arrivalHubId,
        String departureHubName,
        String arrivalHubName,
        Double distance, // 예상 거리
        Long transitTime // 분 단위
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Long timestamp
    ) {}
}