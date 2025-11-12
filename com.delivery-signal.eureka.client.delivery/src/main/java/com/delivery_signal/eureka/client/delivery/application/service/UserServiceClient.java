package com.delivery_signal.eureka.client.delivery.application.service;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * User 서비스 호출 클라이언트
 * 사용자 인가 정보 조회
 */
//@FeignClient(name = "user-service", path = "${internal.user.url}")
@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/authorization")
    AuthUserResponseDto getUserAuthorizationInfo(@RequestHeader("X-User-ID") Long userId);

    // FeignClient 응답 DTO: 외부 서비스의 데이터 모델 (변환 전)
    record AuthUserResponseDto(
        Long userId,
        String role,
        String organization,
        UUID organizationId
    ) {}
}