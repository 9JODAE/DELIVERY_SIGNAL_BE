package com.delivery_signal.eureka.client.hub.infrastructure.redis;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.delivery_signal.eureka.client.hub.domain.repository.DistributedLockManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockAdapter implements DistributedLockManager {

	private final RedissonClient redissonClient;

	private static final String LOCK_PREFIX = "LOCK:";
	private static final long DEFAULT_WAIT_TIME = 5L;
	private static final long DEFAULT_LEASE_TIME = 3L;

	/**
	 * 분산락을 획득하고 비즈니스 로직 실행
	 *
	 * @param lockKey 락 키
	 * @param waitTime 락 획득 대기 시간 (초)
	 * @param leaseTime 락 유지 시간 (초)
	 * @param supplier 실행할 비즈니스 로직
	 */
	public <T> T executeWithLock(String lockKey, long waitTime, long leaseTime, Supplier<T> supplier) {
		RLock lock = redissonClient.getLock(LOCK_PREFIX + lockKey);

		try {
			boolean acquired = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

			if (!acquired) {
				log.warn("락 획득 실패 - lockKey: {}", lockKey);
				throw new IllegalStateException("다른 요청이 처리 중입니다. 잠시 후 다시 시도해주세요.");
			}

			log.debug("락 획득 성공 - lockKey: {}", lockKey);
			return supplier.get();

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("락 획득 중 인터럽트 발생 - lockKey: {}", lockKey, e);
			throw new IllegalStateException("요청 처리 중 오류가 발생했습니다.", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
				log.debug("락 해제 완료 - lockKey: {}", lockKey);
			}
		}
	}

	/**
	 * 기본 설정으로 분산락 실행
	 */
	public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
		return executeWithLock(lockKey, DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, supplier);
	}
}
