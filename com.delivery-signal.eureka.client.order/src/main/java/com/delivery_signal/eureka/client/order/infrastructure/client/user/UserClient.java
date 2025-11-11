package com.delivery_signal.eureka.client.order.infrastructure.client.user;

import com.delivery_signal.eureka.client.order.domain.vo.user.UserAuthorizationInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * User 서비스 내부 호출 클라이언트
 * - 사용자 인증/인가 정보 조회
 */
@FeignClient(name = "user-service", url = "${internal.user.url}")
public interface UserClient {

    /**
     * 사용자 검증 + Role 정보 조회
     */
    @GetMapping("/open-api/v1/users/authorization")
    UserAuthorizationInfo getUserAuthorizationInfo(@RequestParam("userId") Long userId);
}
