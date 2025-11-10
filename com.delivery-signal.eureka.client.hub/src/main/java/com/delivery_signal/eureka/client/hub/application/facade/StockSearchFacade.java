package com.delivery_signal.eureka.client.hub.application.facade;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.delivery_signal.eureka.client.hub.application.HubService;
import com.delivery_signal.eureka.client.hub.application.command.SearchStockCommand;
import com.delivery_signal.eureka.client.hub.application.dto.StockDetailResult;
import com.delivery_signal.eureka.client.hub.application.dto.external.ProductInfo;
import com.delivery_signal.eureka.client.hub.application.port.ProductClient;
import com.delivery_signal.eureka.client.hub.domain.model.Stock;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockSearchFacade {

	private final HubService hubService;
	private final ProductClient productClient;

	@CircuitBreaker(name = "stockSearchCircuitBreaker", fallbackMethod = "searchStocksFallback")
	public Page<StockDetailResult> searchStocks(SearchStockCommand command) {
		Map<UUID, ProductInfo> productInfos = getProducts(command);
		Page<Stock> stocks = hubService.findStocks(command, productInfos.keySet().stream().toList());
		return mapToResults(stocks, productInfos);
	}

	/**
	 * CircuitBreaker fallback - 상품 서비스 호출 실패 시 재고 정보만 반환
	 */
	public Page<StockDetailResult> searchStocksFallback(SearchStockCommand command, Throwable throwable) {
		log.error("상품 서비스 호출 실패: {}, 요청 정보 - 허브 ID: {}, 상품명: {}",
			throwable.getMessage(),
			command.hubId(),
			command.productName());

		try {
			Page<Stock> stocks = hubService.findStocks(command, List.of());
			return mapToResults(stocks);
		} catch (Exception e) {
			log.error("Fallback 처리 중 오류 발생 : {}", e.getMessage());
			return Page.empty();
		}
	}

	private Map<UUID, ProductInfo> getProducts(SearchStockCommand command) {
		if (!StringUtils.hasText(command.productName())) {
			return Map.of();
		}

		return productClient.searchProducts(command.hubId(), command.productName());
	}

	private Page<StockDetailResult> mapToResults(Page<Stock> stocks, Map<UUID, ProductInfo> productInfos) {
		if (stocks.isEmpty()) {
			return Page.empty();
		}

		List<StockDetailResult> results = stocks.stream()
			.map(stock -> createStockResult(stock, productInfos))
			.filter(Objects::nonNull)
			.toList();

		return new PageImpl<>(results, stocks.getPageable(), stocks.getTotalElements());
	}

	private StockDetailResult createStockResult(Stock stock, Map<UUID, ProductInfo> productInfos) {
		UUID productId = stock.getProductId().getValue();
		ProductInfo productInfo = productInfos.get(productId);
		return productInfo != null ? StockDetailResult.from(stock, productInfo) : null;
	}

	private Page<StockDetailResult> mapToResults(Page<Stock> stocks) {
		if (stocks.isEmpty()) {
			return Page.empty();
		}

		List<StockDetailResult> results = stocks.stream()
			.map(stock -> StockDetailResult.of(
				stock.getStockId(),
				stock.getProductId().getValue(),
				"상품 정보 조회 불가",
				BigDecimal.valueOf(-1),
				stock.getQuantity()
			))
			.toList();

		return new PageImpl<>(results, stocks.getPageable(), stocks.getTotalElements());
	}

}