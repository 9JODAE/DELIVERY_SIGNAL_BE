package com.delivery_signal.eureka.client.order.infrastructure.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** TODO [공용] Redis 분산 락 및 캐시 설정용 Config
 *
 * 이 클래스는 다른 도메인 서비스에서도 그대로 복사해서 써도 됩니다.
 *    단, 로컬 테스트 환경에서는 Redis 컨테이너가 띄워져 있어야 하며,
 *    주소는 "localhost:6379" 기준으로 맞춰져 있습니다.
 *
 * 참고
 *  - Docker 환경에서 Redis 컨테이너 실행 명령어:
 *      docker run -d --name redis -p 6379:6379 redis:latest
 *  - 이후 localhost:6379로 접속 가능
 */
@Configuration
public class RedissonConfig {

    /**
     * [공용으로 가져가야 할 부분]
     * RedissonClient Bean 생성 로직.
     *
     * - 다른 서비스에서도 그대로 복사해서 사용 가능.
     * - Redisson은 스프링에서 Redis 기반 분산락, 캐시 등에 사용됩니다.
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 로컬 개발 환경에서는 localhost 기준.
        // Docker Compose 등에서 서비스명 redis-service를 쓸 때는 이 부분만 수정하면 됨.
        config.useSingleServer()
                .setAddress("redis://localhost:6379");

        return Redisson.create(config);
    }
}
