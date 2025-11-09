package com.delivery_signal.eureka.client.hub.domain.repository;

import java.util.function.Supplier;

public interface DistributedLockManager {

	<T> T executeWithLock(String lockKey, Supplier<T> supplier);
}
