// java
package com.delivery_signal.eureka.client.hub.infrastructure.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private static final long LOCK_WAIT_TIME_SECONDS = 5L;
    private static final long LOCK_LEASE_TIME_DISABLED = -1L;

    @Override
    public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
        validateLockKey(lockKey);
        return executeWithSortedLocks(Collections.singletonList(lockKey), supplier);
    }

    @Override
    public <T> T executeWithLocks(List<String> lockKeys, Supplier<T> supplier) {
        validateLockKeys(lockKeys);
        return executeWithSortedLocks(lockKeys, supplier);
    }

    private <T> T executeWithSortedLocks(List<String> rawKeys, Supplier<T> supplier) {
        List<String> sortedKeys = normalizeLockKeys(rawKeys);

        if (sortedKeys.isEmpty()) {
            return supplier.get();
        }

        List<RLock> acquiredLocks = acquireLocksOrThrow(sortedKeys);

        try {
            log.info("비즈니스 로직 실행 시작 - keys={}", sortedKeys);
            return supplier.get();
        } finally {
            releaseAllLocks(acquiredLocks);
            log.info("락 해제 완료 - keys={}", sortedKeys);
        }
    }

    private List<String> normalizeLockKeys(List<String> rawKeys) {
        return rawKeys.stream()
            .filter(key -> key != null && !key.trim().isEmpty())
            .map(String::trim)
            .distinct()
            .sorted()
            .toList();
    }

    private List<RLock> acquireLocksOrThrow(List<String> keys) {
        List<RLock> acquiredLocks = new ArrayList<>(keys.size());

        try {
            for (String key : keys) {
                RLock lock = acquireLockWithTimeout(key);
                acquiredLocks.add(lock);
            }

            log.info("분산 락 획득 완료 - keys={}", keys);
            return acquiredLocks;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            releaseAllLocks(acquiredLocks);
            throw new IllegalStateException("락 획득 중 인터럽트 발생", e);
        } catch (LockAcquisitionFailedException e) {
            releaseAllLocks(acquiredLocks);
            throw e;
        }
    }

    private RLock acquireLockWithTimeout(String key) throws InterruptedException {
        RLock lock = redissonClient.getLock(LOCK_PREFIX + key);

        boolean acquired = lock.tryLock(
            LOCK_WAIT_TIME_SECONDS,
            LOCK_LEASE_TIME_DISABLED,
            TimeUnit.SECONDS
        );

        if (!acquired) {
            log.warn("락 획득 실패 - key={}", key);
            throw new LockAcquisitionFailedException(key);
        }

        return lock;
    }

    private void releaseAllLocks(List<RLock> locks) {
        for (int i = locks.size() - 1; i >= 0; i--) {
            releaseLockSafely(locks.get(i));
        }
    }

    private void releaseLockSafely(RLock lock) {
        try {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (Exception e) {
            log.error("락 해제 실패 - lock={}", lock.getName(), e);
        }
    }

    private void validateLockKey(String lockKey) {
        if (lockKey == null || lockKey.isBlank()) {
            throw new IllegalArgumentException("락 키는 null이거나 공백일 수 없습니다.");
        }
    }

    private void validateLockKeys(List<String> lockKeys) {
        if (lockKeys == null) {
            throw new IllegalArgumentException("락 키 리스트는 null일 수 없습니다.");
        }
    }

    private static class LockAcquisitionFailedException extends RuntimeException {
        public LockAcquisitionFailedException(String key) {
            super(String.format("현재 다른 작업이 진행 중입니다. (key=%s)", key));
        }
    }
}