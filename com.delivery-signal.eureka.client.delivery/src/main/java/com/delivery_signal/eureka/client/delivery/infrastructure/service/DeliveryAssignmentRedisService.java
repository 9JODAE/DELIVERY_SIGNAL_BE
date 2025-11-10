package com.delivery_signal.eureka.client.delivery.infrastructure.service;

import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryManagerRepository;
import com.delivery_signal.eureka.client.delivery.domain.service.DeliveryAssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliveryAssignmentRedisService implements DeliveryAssignmentService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final DeliveryManagerRepository deliveryManagerRepository;

    // 레디스에 순번을 저장할 때 사용할 키(key)
    private static final String LAST_SEQUENCE_KEY = "delivery:last-sequence";

    public DeliveryAssignmentRedisService(RedisTemplate<String, Object> redisTemplate,
        DeliveryManagerRepository deliveryManagerRepository) {
        this.redisTemplate = redisTemplate;
        this.deliveryManagerRepository = deliveryManagerRepository;
    }

    @Override
    public DeliveryManager getNextManagerForAssignment() {
        // 레디스에서 직전 순번 조회
        int lastSequence = getLastAssignedSequence();

        // 다음 배송 담당자 조회
        DeliveryManager nextManager = deliveryManagerRepository.findNextActiveManager(lastSequence)
            .orElseThrow(() -> new IllegalStateException("배정 로직 오류: 다음 담당자를 찾을 수 없습니다."));

        // 배정된 담당자의 순번을 Redis에 업데이트 (마지막 순번 업데이트)
        redisTemplate.opsForValue().set(LAST_SEQUENCE_KEY, String.valueOf(nextManager.getDeliverySequence()));
        return nextManager;
    }

    // 순번을 안전하게 가져오기 (Redis 장애 대비 및 NULL 처리)
    private int getLastAssignedSequence() {
        String lastSequenceStr = (String) redisTemplate.opsForValue().get(LAST_SEQUENCE_KEY);
        if (lastSequenceStr == null) {
            return -1; // 초기값
        }
        try {
            return Integer.parseInt(lastSequenceStr);
        } catch (NumberFormatException e) {
            log.error("[Redis] 순번 KEY({})의 값이 유효하지 않습니다. 기본값 -1 반환.", LAST_SEQUENCE_KEY, e);
            return -1;
        }
    }
}
