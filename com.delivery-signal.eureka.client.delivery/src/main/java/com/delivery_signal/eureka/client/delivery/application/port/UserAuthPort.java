package com.delivery_signal.eureka.client.delivery.application.port;

import com.delivery_signal.eureka.client.delivery.domain.vo.AuthorizedUser;

/**
 * Port: Delivery Domain이 User Context로부터 사용자 권한 정보를 요청하는 추상화된 인터페이스 (Output Port)
 * Delivery 도메인의 요구사항을 정의
 */
public interface UserAuthPort {
    /**
     * 특정 사용자 ID의 인가 및 활성 정보를 조회
     */
    AuthorizedUser fetchUserAuthorizationInfo(Long userId);
}
