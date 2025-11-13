package com.delivery_signal.eureka.client.company.infrastructure.client;


import com.delivery_signal.eureka.client.company.application.dto.ApiResponse;
import com.delivery_signal.eureka.client.company.infrastructure.client.response.UserAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * User 서비스 내부 호출 클라이언트
 * - 사용자 인증/인가 정보 조회
 */
@FeignClient(name = "user-service", url = "${internal.user.url}")
public interface UserClient {

    @GetMapping("/auth/authorization")
    ApiResponse<UserAuthResponse> getUserAuthorizationInfo(@RequestHeader("x-user-id") String userId);

}
