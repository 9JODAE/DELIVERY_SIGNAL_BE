package com.delivery_signal.eureka.client.hub.domain.repository;

import org.springframework.data.domain.Page;

import com.delivery_signal.eureka.client.hub.domain.condition.StockSearchCondition;
import com.delivery_signal.eureka.client.hub.domain.entity.Stock;

public interface StockSearchRepository {

	/**
	 * 재고 검색
	 * @param condition 검색 조건
	 * @return 재고 목록
	 */
	Page<Stock> searchStocks(StockSearchCondition condition);
}
