package com.delivery_signal.eureka.client.hub.application;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_signal.eureka.client.hub.application.command.CreateHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.CreateHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.CreateStockCommand;
import com.delivery_signal.eureka.client.hub.application.command.DeductStockQuantityCommand;
import com.delivery_signal.eureka.client.hub.application.command.DeleteStockCommand;
import com.delivery_signal.eureka.client.hub.application.command.RestoreStockQuantityCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchStockCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateStockCommand;
import com.delivery_signal.eureka.client.hub.application.dto.HubResult;
import com.delivery_signal.eureka.client.hub.application.dto.HubRouteResult;
import com.delivery_signal.eureka.client.hub.application.dto.StockResult;
import com.delivery_signal.eureka.client.hub.common.annotation.PreAuthorize;
import com.delivery_signal.eureka.client.hub.common.auth.Authority;
import com.delivery_signal.eureka.client.hub.domain.mapper.StockSearchCondition;
import com.delivery_signal.eureka.client.hub.domain.model.Hub;
import com.delivery_signal.eureka.client.hub.domain.model.HubRoute;
import com.delivery_signal.eureka.client.hub.domain.model.Stock;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRouteReadRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.StockReadRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.HubQueryRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRouteQueryRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.StockQueryRepository;
import com.delivery_signal.eureka.client.hub.domain.vo.Address;
import com.delivery_signal.eureka.client.hub.domain.vo.Coordinate;
import com.delivery_signal.eureka.client.hub.domain.vo.Distance;
import com.delivery_signal.eureka.client.hub.domain.vo.Duration;
import com.delivery_signal.eureka.client.hub.domain.mapper.HubRouteSearchCondition;
import com.delivery_signal.eureka.client.hub.domain.mapper.HubSearchCondition;
import com.delivery_signal.eureka.client.hub.domain.vo.ProductId;
import com.delivery_signal.eureka.client.hub.common.annotation.CacheEvict;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HubService {

	private final HubRepository hubRepository;
	private final HubQueryRepository hubQueryRepository;
	private final HubRouteQueryRepository hubRouteQueryRepository;
	private final HubRouteReadRepository hubRouteReadRepository;
	private final StockQueryRepository stockQueryRepository;
	private final StockReadRepository stockReadRepository;

	/**
	 * 허브 생성
	 * @param command 허브 생성 커맨드
	 * @return 저장된 허브의 아이디
	 */
	@PreAuthorize({Authority.MASTER})
	public UUID createHub(CreateHubCommand command) {
		Address address = Address.of(command.address());
		Coordinate coordinate = Coordinate.of(command.latitude(), command.longitude());
		Hub savedHub = hubRepository.save(Hub.create(command.name(), address, coordinate));
		return savedHub.getHubId();
	}

	/**
	 * 허브 검색
	 * @param command 허브 검색 커맨드
	 * @return Page<HubResult> 허브 검색 결과
	 */
	@PreAuthorize({Authority.MASTER, Authority.HUB_MANAGER, Authority.DELIVERY_MANAGER, Authority.SUPPLIER_MANAGER})
	@Transactional(readOnly = true)
	public Page<HubResult> searchHubs(SearchHubCommand command) {
		HubSearchCondition condition = HubSearchCondition.of(
			command.name(),
			command.address(),
			command.page(),
			command.size(),
			command.sortBy(),
			command.direction()
		);

		return hubQueryRepository.searchHubs(condition)
			.map(HubResult::from);
	}

	/**
	 * 허브 조회
	 * @param hubId 허브 아이디
	 * @return 허브 조회 결과
	 */
	@PreAuthorize({Authority.MASTER, Authority.HUB_MANAGER, Authority.DELIVERY_MANAGER, Authority.SUPPLIER_MANAGER})
	@Transactional(readOnly = true)
	public HubResult getHub(UUID hubId) {
		Hub hub = getHubOrThrow(hubId);
		return HubResult.from(hub);
	}

	/**
	 * 허브 존재 여부 확인 (내부 API)
	 * @param hubId 허브 아이디
	 * @return 존재 여부
	 */
	public boolean existsHub(UUID hubId) {
		return hubRepository.exists(hubId);
	}

	/**
	 * 허브 수정
	 * @param command 허브 수정 커맨드
	 * @return 수정된 허브 결과
	 */
	@PreAuthorize({Authority.MASTER})
	public HubResult updateHub(UpdateHubCommand command) {
		Hub hub = getHubOrThrow(command.hubId());
		Address address = Address.of(command.address());
		Coordinate coordinate = Coordinate.of(command.latitude(), command.longitude());
		hub.update(command.name(), address, coordinate);
		return HubResult.from(hub);
	}

	/**
	 * 허브 삭제
	 * @param hubId 허브 아이디
	 */
	@PreAuthorize({Authority.MASTER})
	@CacheEvict(value = "hubRoutes", allEntries = true)
	public void deleteHub(UUID hubId) {
		Hub hub = getHubOrThrow(hubId);
		hub.delete(1L); // TODO 유저 서비스 개발 완료 시 변경
	}

	public Hub getHubOrThrow(UUID hubId) {
		return hubRepository.findById(hubId)
			.orElseThrow(() -> new IllegalArgumentException("허브를 찾을 수 없습니다. hubId=" + hubId));
	}

	/**
	 * 허브 이동정보 생성
	 * @param command 허브 경로 생성 커맨드
	 * @return 생성된 허브 경로 아이디
	 */
	@PreAuthorize({Authority.MASTER})
	@CacheEvict(value = "hubRoutes", allEntries = true)
	public UUID createHubRoute(CreateHubRouteCommand command) {
		Hub departureHub = getHubOrThrow(command.departureHubId());
		Hub arrivalHub = getHubOrThrow(command.arrivalHubId());
		Distance distance = Distance.of(command.distance());
		Duration transitTime = Duration.of(command.transitTime());

		HubRoute route = HubRoute.create(departureHub, arrivalHub, distance, transitTime);
		departureHub.addHubRoute(route);
		return route.getHubRouteId();
	}

	/**
	 * 허브 이동정보 검색
	 * @param command 허브 경로 검색 커맨드
	 * @return Page<HubRouteResult> 허브 경로 검색 결과
	 */
	@PreAuthorize({Authority.MASTER, Authority.HUB_MANAGER, Authority.DELIVERY_MANAGER, Authority.SUPPLIER_MANAGER})
	@Transactional(readOnly = true)
	public Page<HubRouteResult> searchHubRoutes(SearchHubRouteCommand command) {
		HubRouteSearchCondition condition = HubRouteSearchCondition.of(
			command.departureHubName(),
			command.arrivalHubName(),
			command.page(),
			command.size(),
			command.sortBy(),
			command.direction()
		);
		return hubRouteQueryRepository.searchHubRoutes(condition)
			.map(HubRouteResult::from);
	}

	/**
	 * 허브 이동정보 조회
	 * @param hubId 출발 허브 아이디
	 * @param hubRouteId 허브 이동정보 아이디
	 * @return 허브 이동정보 조회 결과
	 */
	@PreAuthorize({Authority.MASTER, Authority.HUB_MANAGER, Authority.DELIVERY_MANAGER, Authority.SUPPLIER_MANAGER})
	@Transactional(readOnly = true)
	public HubRouteResult getHubRoute(UUID hubId, UUID hubRouteId) {
		Hub hub = getHubWithRoutesOrThrow(hubId);
		HubRoute route = hub.getHubRoute(hubRouteId);
		return HubRouteResult.from(route);
	}

	/**
	 * 허브 이동정보 수정
	 * @param hubId 출발 허브 아이디
	 * @param hubRouteId 허브 이동정보 아이디
	 * @param command 허브 이동정보 수정 커맨드
	 * @return 수정된 허브 이동정보 결과
	 */
	@PreAuthorize({Authority.MASTER})
	@CacheEvict(value = "hubRoutes", allEntries = true)
	public HubRouteResult updateHubRoute(UUID hubId, UUID hubRouteId, UpdateHubRouteCommand command) {
		Hub hub = getHubWithRoutesOrThrow(hubId);
		Distance distance = Distance.of(command.distance());
		Duration transitTime = Duration.of(command.transitTime());
		HubRoute route = hub.updateHubRoute(hubRouteId, distance, transitTime);
		return HubRouteResult.from(route);
	}

	/**
	 * 허브 이동정보 삭제
	 * @param hubId 출발 허브 아이디
	 * @param hubRouteId 허브 이동정보 아이디
	 */
	@PreAuthorize({Authority.MASTER})
	@CacheEvict(value = "hubRoutes", allEntries = true)
	public void deleteHubRoute(UUID hubId, UUID hubRouteId) {
		Hub hub = getHubWithRoutesOrThrow(hubId);
		hub.deleteHubRoute(hubRouteId, 1L); // TODO 유저 서비스 개발 완료 시 변경
	}

	private Hub getHubWithRoutesOrThrow(UUID hubId) {
		return hubRepository.findByIdWithRoutes(hubId)
			.orElseThrow(() -> new IllegalArgumentException("허브를 찾을 수 없습니다. hubId=" + hubId));
	}

	/**
	 * 모든 허브 이동정보 조회
	 * @return List<HubRoute> 모든 허브 이동정보 리스트
	 */
	@Transactional(readOnly = true)
	public List<HubRoute> getRoutes() {
		return hubRouteReadRepository.getRoutes();
	}

	/**
	 * 특정 허브 이동정보 조회
	 * @param routeIds 허브 이동정보 아이디 리스트
	 * @return List<HubRoute> 순서가 보장된 허브 이동정보 리스트
	 */
	@Transactional(readOnly = true)
	public List<HubRoute> getRoutes(List<UUID> routeIds) {
		List<HubRoute> routes = hubRouteReadRepository.getRoutes(routeIds);

		if (routeIds.size() != routes.size()) {
			throw new IllegalArgumentException("일부 허브 이동정보를 찾을 수 없습니다.");
		}

		Map<UUID, HubRoute> routeMap = routes.stream()
			.collect(Collectors.toMap(HubRoute::getHubRouteId, route -> route));

		return routeIds.stream()
			.map(routeMap::get)
			.collect(Collectors.toList());
	}

	/**
	 * 재고 생성
	 * @param command 재고 생성 커맨드
	 * @return 생성된 재고 아이디
	 */
	@PreAuthorize({Authority.MASTER, Authority.HUB_MANAGER})
	public UUID createStock(CreateStockCommand command) {
		// TODO 상품 서비스로부터 상품 존재 유무 확인하는 로직
		// if (!productClient.exists(command.productId())) {
		// 	throw new IllegalArgumentException("존재하지 않는 상품입니다. productId=" + command.productId());
		// }

		Hub hub = getHubOrThrow(command.hubId());
		ProductId productId = ProductId.of(command.productId());

		Stock stock = Stock.create(hub, productId, command.quantity());
		hub.addStock(stock);
		return stock.getStockId();
	}

	/**
	 * 재고 검색
	 * @param command 재고 검색 커맨드
	 * @param productIds 상품 아이디 리스트
	 * @return Page<Stock> 재고 검색 결과
	 */
	@PreAuthorize({Authority.MASTER, Authority.HUB_MANAGER})
	@Transactional(readOnly = true)
	public Page<Stock> findStocks(SearchStockCommand command, List<UUID> productIds) {
		StockSearchCondition condition = StockSearchCondition.of(
			command.hubId(),
			productIds,
			command.page(),
			command.size(),
			command.sortBy(),
			command.direction()
		);

		return stockQueryRepository.searchStocks(condition);
	}

	/**
	 * 재고 수량 조회 (내부 API)
	 * @param productIds 상품 아이디 List
	 * @return Map<UUID, Integer> 상품 아이디별 수량 Map
	 */
	public Map<UUID, Integer> getStockQuantities(List<UUID> productIds) {
		List<Stock> stocks = getStocksByProductIdsOrThrow(productIds);
		return stocks.stream()
			.collect(Collectors.toMap(
				stock -> stock.getProductId().getValue(),
				Stock::getQuantity
			));
	}

	/**
	 * 재고 수정
	 * @param command 재고 수정 Command
	 * @return 수정된 재고 결과
	 */
	@PreAuthorize({Authority.MASTER, Authority.HUB_MANAGER})
	public StockResult updateStock(UpdateStockCommand command) {
		Hub hub = getHubWithStocksOrThrow(command.hubId());
		Stock stock = hub.updateStockQuantity(command.stockId(), command.quantity());

		log.info("재고 수정 완료. hubId={}, stockId={}, quantity={}",
			command.hubId(), command.stockId(), stock.getQuantity());

		return StockResult.from(stock);
	}

	/**
	 * 재고 삭제
	 * @param command 재고 삭제 Command
	 */
	@PreAuthorize({Authority.MASTER, Authority.HUB_MANAGER})
	public void deleteStock(DeleteStockCommand command) {
		Hub hub = getHubWithStocksOrThrow(command.hubId());
		hub.deleteStock(command.stockId(), 1L); // TODO 유저 서비스 개발 완료 시 변경
	}

	/**
	 * 재고 차감
	 * @param command 재고 차감 Command
	 * @param productToStockId 상품 아이디별 재고 아이디 Map
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deductStocks(DeductStockQuantityCommand command, Map<UUID, UUID> productToStockId) {
		Hub hub = getHubWithStocksOrThrow(command.hubId());

		command.items().forEach((productId, quantity) ->
			deductStock(hub, productId, quantity, productToStockId.get(productId))
		);
	}

	/**
	 * 재고 복구
	 * @param command 재고 복구 Command
	 * @param productToStockId 상품 아이디별 재고 아이디 Map
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void restoreStocks(RestoreStockQuantityCommand command, Map<UUID, UUID> productToStockId) {
		Hub hub = getHubWithStocksOrThrow(command.hubId());

		command.items().forEach((productId, quantity) ->
			restoreStock(hub, productId, quantity, productToStockId.get(productId))
		);
	}

	private void deductStock(Hub hub, UUID productId, int quantity, UUID stockId) {
		try {
			hub.deductStocks(stockId, quantity);
			log.info("재고 차감 완료 - productId={}, stockId={}, quantity={}", productId, stockId, quantity);
		} catch (IllegalArgumentException ex) {
			log.error("재고 부족 - productId={}, 요청수량={}", productId, quantity);
			throw new IllegalStateException("재고가 부족합니다. productId=" + productId);
		}
	}

	private void restoreStock(Hub hub, UUID productId, int quantity, UUID stockId) {
		try {
			hub.restoreStocks(stockId, quantity);
			log.info("재고 복구 완료 - productId={}, stockId={}, quantity={}", productId, stockId, quantity);
		} catch (IllegalArgumentException ex) {
			throw new IllegalStateException("재고 복구 실패. productId=" + productId, ex);
		}
	}

	private Hub getHubWithStocksOrThrow(UUID hubId) {
		return hubRepository.findByIdWithStocks(hubId)
			.orElseThrow(() -> new IllegalArgumentException("허브를 찾을 수 없습니다. hubId=" + hubId));
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Stock> getStocksByProductIdsOrThrow(List<UUID> productIds) {
		List<Stock> stocks = stockReadRepository.getStocks(productIds);

		if (productIds.size() != stocks.size()) {
			throw new IllegalArgumentException("일부 재고를 찾을 수 없습니다.");
		}

		return stocks;
	}
}
