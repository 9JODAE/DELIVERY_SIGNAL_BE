package com.delivery_signal.eureka.client.hub.domain.repository;

import java.util.List;
import java.util.function.Supplier;

public interface DistributedLockManager {

	<T> T executeWithLock(String lockKey, Supplier<T> supplier);

	<T> T executeWithLocks(List<String> lockKeys, Supplier<T> supplier);
}
