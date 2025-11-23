package com.delivery_signal.eureka.client.hub.infrastructure.config;

import java.util.Optional;

import com.delivery_signal.eureka.client.hub.common.auth.InternalRequestContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.delivery_signal.eureka.client.hub.common.auth.UserContextHolder;
import com.delivery_signal.eureka.client.hub.common.error.HubErrorCode;
import com.delivery_signal.eureka.client.hub.common.exception.UnauthorizedException;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

    @Value("${audit.system-master-id}")
    private Long systemMasterId;

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            // 내부 통신이면 SYSTEM MASTER로 처리
            if (InternalRequestContextHolder.isInternal()) {
                return Optional.of(systemMasterId);
            }

            // 외부 요청이면 UserContextHolder에서 userId 가져오기
            String userId = UserContextHolder.getUserId();
            if (userId == null || userId.isBlank()) {
                throw new UnauthorizedException(HubErrorCode.UNAUTHORIZED);
            }

            return Optional.of(Long.parseLong(userId));
        };
    }
}
