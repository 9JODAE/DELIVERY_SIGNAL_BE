package com.delivery_signal.eureka.client.hub.domain.repository;

import java.util.List;
import java.util.UUID;

import com.delivery_signal.eureka.client.hub.domain.entity.Stock;

public interface StockReadRepository {

	/**
	 * 제품 ID 목록에 해당하는 재고 조회
	 * @param productIds 제품 ID 목록
	 * @return 재고 목록
	 */
	List<Stock> getStocks(List<UUID> productIds);
}
