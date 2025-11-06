package com.delivery_signal.eureka.client.user.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditorConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // 실제 구현 시 현재 로그인한 유저 ID를 반환하면 됨
        return () -> Optional.of("SYSTEM"); // 테스트용
    }
}
