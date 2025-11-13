package com.delivery_signal.eureka.client.hub.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;

import com.delivery_signal.eureka.client.hub.domain.vo.Address;
import com.delivery_signal.eureka.client.hub.domain.vo.Coordinate;
import com.delivery_signal.eureka.client.hub.domain.vo.Distance;
import com.delivery_signal.eureka.client.hub.domain.vo.Duration;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Entity
@Table(name = "p_hubs")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hub extends AggregateRootEntity<Hub> {

	@Id
	@UuidGenerator
	private UUID hubId;

	@Column(nullable = false)
	private String name;

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "address", nullable = false))
	private Address address;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "latitude", column = @Column(name = "latitude", nullable = false)),
		@AttributeOverride(name = "longitude", column = @Column(name = "longitude", nullable = false))
	})
	private Coordinate coordinate;

	@OneToMany(mappedBy = "departureHub", cascade = CascadeType.ALL)
	private List<HubRoute> hubRoutes = new ArrayList<>();

	@OneToMany(mappedBy = "hub", cascade = CascadeType.ALL)
	private List<Stock> stocks = new ArrayList<>();

	public static Hub create(String name, Address address, Coordinate coordinate) {
		Hub hub = new Hub();
		hub.name = name;
		hub.address = address;
		hub.coordinate = coordinate;
		return hub;
	}

	public void update(String name, Address address, Coordinate coordinate) {
		this.name = name;
		this.address = address;
		this.coordinate = coordinate;
	}

	public void delete(Long userId) {
		this.hubRoutes.forEach(route -> route.softDelete(userId));
		this.softDelete(userId);
	}

	public void addHubRoute(HubRoute route) {
		this.hubRoutes.add(route);
	}

	public HubRoute getHubRoute(UUID hubRouteId) {
		return findHubRouteById(hubRouteId);
	}

	public HubRoute updateHubRoute(UUID hubRouteId, Distance distance, Duration transitTime) {
		HubRoute route = findHubRouteById(hubRouteId);
		route.update(distance, transitTime);
		return route;
	}

	public void deleteHubRoute(UUID hubRouteId, Long userId) {
		HubRoute route = findHubRouteById(hubRouteId);
		route.softDelete(userId);
	}

	private HubRoute findHubRouteById(UUID hubRouteId) {
		return this.hubRoutes.stream()
			.filter(route -> route.getHubRouteId().equals(hubRouteId))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("요청한 허브 이동정보를 찾을 수 없습니다."));
	}

	public void addStock(Stock stock) {
		this.stocks.add(stock);
	}

	public Stock updateStockQuantity(UUID stockId, int quantity) {
		Stock stock = findStockById(stockId);
		stock.updateQuantity(quantity);
		return stock;
	}

	private Stock findStockById(UUID stockId) {
		return this.stocks.stream()
			.filter(stock -> stock.getStockId().equals(stockId))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("요청한 재고를 찾을 수 없습니다."));
	}

	public void deductStocks(UUID stockId, int quantity) {
		Stock stock = findStockById(stockId);
		stock.deduct(quantity);
	}

	public void restoreStocks(UUID stockId, int quantity) {
		Stock stock = findStockById(stockId);
		stock.restore(quantity);
	}

	public void deleteStock(UUID stockId, Long userId) {
		Stock stock = findStockById(stockId);
		stock.delete(userId);
	}
}
