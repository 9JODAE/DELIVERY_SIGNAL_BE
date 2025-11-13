package com.delivery_signal.eureka.client.hub.application.facade;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_signal.eureka.client.hub.application.HubService;
import com.delivery_signal.eureka.client.hub.application.command.DeductStockQuantityCommand;
import com.delivery_signal.eureka.client.hub.application.command.RestoreStockQuantityCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateStockCommand;
import com.delivery_signal.eureka.client.hub.application.dto.StockResult;
import com.delivery_signal.eureka.client.hub.domain.entity.Stock;
import com.delivery_signal.eureka.client.hub.domain.repository.DistributedLockManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockUpdateFacade {
	private static final String STOCK_LOCK_PREFIX = "STOCK:";

	private final HubService hubService;
	private final DistributedLockManager distributedLockManager;

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public StockResult updateStock(UpdateStockCommand command) {
		String lockKey = STOCK_LOCK_PREFIX + command.stockId();

		return distributedLockManager.executeWithLock(lockKey,
			() -> hubService.updateStock(command));
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void deductStocks(DeductStockQuantityCommand command) {
		executeWithDistributedLock(command.items().keySet().stream().toList(),
			productToStock -> hubService.deductStocks(command, productToStock));
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void restoreStocks(RestoreStockQuantityCommand command) {
		executeWithDistributedLock(command.items().keySet().stream().toList(),
			productToStock -> hubService.restoreStocks(command, productToStock));
	}

	private void executeWithDistributedLock(List<UUID> productIds, StockOperation operation) {
		Map<UUID, UUID> productToStock = fetchProductToStockMapping(productIds);
		List<String> lockKeys = createSortedLockKeys(productToStock);

		distributedLockManager.executeWithLocks(lockKeys, () -> {
			operation.execute(productToStock);
			return null;
		});
	}

	private Map<UUID, UUID> fetchProductToStockMapping(List<UUID> productIds) {
		List<Stock> stocks = hubService.getStocksByProductIdsOrThrow(productIds);
		return stocks.stream()
			.collect(Collectors.toMap(
				stock -> stock.getProductId().getValue(),
				Stock::getStockId
			));
	}

	private List<String> createSortedLockKeys(Map<UUID, UUID> productToStock) {
		return productToStock.values().stream()
			.map(stockId -> STOCK_LOCK_PREFIX + stockId)
			.sorted()
			.toList();
	}

	@FunctionalInterface
	private interface StockOperation {
		void execute(Map<UUID, UUID> productToStock);
	}
}