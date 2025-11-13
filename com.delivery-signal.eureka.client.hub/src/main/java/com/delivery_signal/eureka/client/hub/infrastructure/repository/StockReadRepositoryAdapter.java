package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.delivery_signal.eureka.client.hub.domain.entity.Stock;
import com.delivery_signal.eureka.client.hub.domain.repository.StockReadRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockReadRepositoryAdapter implements StockReadRepository {

	private final JpaStockRepository jpaStockRepository;

	@Override
	public List<Stock> getStocks(List<UUID> productIds) {
		return jpaStockRepository.findByProductIdIn(productIds);
	}
}
