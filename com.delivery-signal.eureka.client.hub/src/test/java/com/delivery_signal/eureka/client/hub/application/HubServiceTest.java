package com.delivery_signal.eureka.client.hub.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.delivery_signal.eureka.client.hub.application.command.CreateHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.CreateHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.CreateStockCommand;
import com.delivery_signal.eureka.client.hub.application.command.DeductStockQuantityCommand;
import com.delivery_signal.eureka.client.hub.application.command.DeleteStockCommand;
import com.delivery_signal.eureka.client.hub.application.command.RestoreStockQuantityCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateStockCommand;
import com.delivery_signal.eureka.client.hub.application.dto.HubResult;
import com.delivery_signal.eureka.client.hub.application.dto.HubRouteResult;
import com.delivery_signal.eureka.client.hub.application.dto.StockResult;
import com.delivery_signal.eureka.client.hub.common.auth.UserContextHolder;
import com.delivery_signal.eureka.client.hub.common.exception.NotFoundException;
import com.delivery_signal.eureka.client.hub.common.exception.OutOfStockException;
import com.delivery_signal.eureka.client.hub.domain.entity.Hub;
import com.delivery_signal.eureka.client.hub.domain.entity.HubRoute;
import com.delivery_signal.eureka.client.hub.domain.entity.Stock;
import com.delivery_signal.eureka.client.hub.domain.repository.HubSearchRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRouteSearchRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRouteReadRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.StockReadRepository;
import com.delivery_signal.eureka.client.hub.domain.vo.Address;
import com.delivery_signal.eureka.client.hub.domain.vo.Coordinate;
import com.delivery_signal.eureka.client.hub.domain.vo.Distance;
import com.delivery_signal.eureka.client.hub.domain.vo.Duration;
import com.delivery_signal.eureka.client.hub.domain.vo.ProductId;

@ExtendWith(MockitoExtension.class)
@DisplayName("HubService 테스트")
class HubServiceTest {

	@Mock
	private HubRepository hubRepository;

	@Mock
	private HubSearchRepository hubQueryRepository;

	@Mock
	private HubRouteSearchRepository hubRouteQueryRepository;

	@Mock
	private HubRouteReadRepository hubRouteReadRepository;

	@Mock
	private StockReadRepository stockReadRepository;

	@InjectMocks
	private HubService hubService;

	private MockedStatic<UserContextHolder> userContextHolder;

	private Hub testHub;
	private UUID testHubId;

	@BeforeEach
	void setUp() {
		testHubId = UUID.randomUUID();
		testHub = createHubWithId(
			testHubId,
			"서울 허브",
			Address.of("서울특별시 강남구 테헤란로 123"),
			Coordinate.of(37.5665, 126.9780)
		);

		userContextHolder = mockStatic(UserContextHolder.class);
		userContextHolder.when(UserContextHolder::getUserId).thenReturn("1");
	}

	@AfterEach
	void tearDown() {
		if (userContextHolder != null) {
			userContextHolder.close();
		}
	}

	private Hub createHubWithId(UUID hubId, String name, Address address, Coordinate coordinate) {
		Hub hub = Hub.create(name, address, coordinate);
		try {
			java.lang.reflect.Field field = Hub.class.getDeclaredField("hubId");
			field.setAccessible(true);
			field.set(hub, hubId);
		} catch (Exception e) {
			throw new RuntimeException("Failed to set hubId", e);
		}
		return hub;
	}

	@Nested
	@DisplayName("허브 생성 테스트")
	class CreateHubTest {

		@Test
		@DisplayName("성공: 허브가 정상적으로 생성된다")
		void createHub_Success() {
			// given
			CreateHubCommand command = CreateHubCommand.of(
				"서울 허브",
				"서울특별시 강남구 테헤란로 123",
				37.5665,
				126.9780
			);

			given(hubRepository.save(any(Hub.class))).willAnswer(invocation -> {
				Hub hub = invocation.getArgument(0);
				// JPA처럼 ID를 세팅해주는 부분
				Field field = Hub.class.getDeclaredField("hubId");
				field.setAccessible(true);
				field.set(hub, UUID.randomUUID());
				return hub;
			});

			// when
			UUID hubId = hubService.createHub(command);

			// then
			assertThat(hubId).isNotNull();
			verify(hubRepository).save(any(Hub.class));
		}
	}

	@Nested
	@DisplayName("허브 조회 테스트")
	class GetHubTest {

		@Test
		@DisplayName("성공: 허브 ID로 허브를 조회한다")
		void getHub_Success() {
			// given
			given(hubRepository.findById(testHubId)).willReturn(Optional.of(testHub));

			// when
			HubResult result = hubService.getHub(testHubId);

			// then
			assertThat(result).isNotNull();
			verify(hubRepository).findById(testHubId);
		}

		@Test
		@DisplayName("실패: 존재하지 않는 허브 ID로 조회 시 예외 발생")
		void getHub_NotFound() {
			// given
			given(hubRepository.findById(testHubId)).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> hubService.getHub(testHubId))
				.isInstanceOf(NotFoundException.class);
		}
	}

	@Nested
	@DisplayName("허브 검색 테스트")
	class SearchHubsTest {

		@Test
		@DisplayName("성공: 검색 조건으로 허브 목록을 조회한다")
		void searchHubs_Success() {
			// given
			SearchHubCommand command = SearchHubCommand.of(
				"서울",
				null,
				0,
				10,
				"createdAt",
				"DESC"
			);

			Page<Hub> hubPage = new PageImpl<>(List.of(testHub));
			given(hubQueryRepository.searchHubs(any())).willReturn(hubPage);

			// when
			Page<HubResult> result = hubService.searchHubs(command);

			// then
			assertThat(result).isNotEmpty();
			assertThat(result.getContent()).hasSize(1);
			verify(hubQueryRepository).searchHubs(any());
		}
	}

	@Nested
	@DisplayName("허브 수정 테스트")
	class UpdateHubTest {

		@Test
		@DisplayName("성공: 허브 정보를 수정한다")
		void updateHub_Success() {
			// given
			UpdateHubCommand command = UpdateHubCommand.of(
				testHubId,
				"부산 허브",
				"부산광역시 해운대구 센텀로 456",
				35.1796,
				129.0756
			);

			given(hubRepository.findById(testHubId)).willReturn(Optional.of(testHub));

			// when
			HubResult result = hubService.updateHub(command);

			// then
			assertThat(result).isNotNull();
			verify(hubRepository).findById(testHubId);
		}
	}

	@Nested
	@DisplayName("허브 삭제 테스트")
	class DeleteHubTest {

		@Test
		@DisplayName("성공: 허브를 소프트 삭제한다")
		void deleteHub_Success() {
			// given
			given(hubRepository.findById(testHubId)).willReturn(Optional.of(testHub));

			// when
			hubService.deleteHub(testHubId);

			// then
			verify(hubRepository).findById(testHubId);
		}
	}

	@Nested
	@DisplayName("허브 존재 확인 테스트")
	class ExistsHubTest {

		@Test
		@DisplayName("성공: 허브가 존재하면 true를 반환한다")
		void existsHub_True() {
			// given
			given(hubRepository.exists(testHubId)).willReturn(true);

			// when
			boolean exists = hubService.existsHub(testHubId);

			// then
			assertThat(exists).isTrue();
			verify(hubRepository).exists(testHubId);
		}

		@Test
		@DisplayName("성공: 허브가 존재하지 않으면 false를 반환한다")
		void existsHub_False() {
			// given
			given(hubRepository.exists(testHubId)).willReturn(false);

			// when
			boolean exists = hubService.existsHub(testHubId);

			// then
			assertThat(exists).isFalse();
			verify(hubRepository).exists(testHubId);
		}
	}

	@Nested
	@DisplayName("허브 경로 생성 테스트")
	class CreateHubRouteTest {

		private UUID arrivalHubId;
		private Hub arrivalHub;

		@BeforeEach
		void setUp() {
			arrivalHubId = UUID.randomUUID();
			arrivalHub = Hub.create(
				"부산 허브",
				Address.of("부산광역시 해운대구 센텀로 456"),
				Coordinate.of(35.1796, 129.0756)
			);
		}

		@Test
		@DisplayName("성공: 허브 간 경로를 생성한다")
		void createHubRoute_Success() {
			// given
			CreateHubRouteCommand command = CreateHubRouteCommand.of(
				testHubId,
				arrivalHubId,
				400.5,
				240
			);

			given(hubRepository.findById(testHubId)).willReturn(Optional.of(testHub));
			given(hubRepository.findById(arrivalHubId)).willReturn(Optional.of(arrivalHub));

			// when
			UUID routeId = hubService.createHubRoute(command);

			// then
			assertThat(routeId).isNotNull();
			verify(hubRepository, times(2)).findById(any(UUID.class));
		}
	}

	@Nested
	@DisplayName("허브 경로 조회 테스트")
	class GetHubRouteTest {

		private UUID routeId;
		private HubRoute hubRoute;

		@BeforeEach
		void setUp() {
			routeId = UUID.randomUUID();
			Hub arrivalHub = Hub.create(
				"부산 허브",
				Address.of("부산광역시 해운대구 센텀로 456"),
				Coordinate.of(35.1796, 129.0756)
			);

			hubRoute = HubRoute.create(
				testHub,
				arrivalHub,
				Distance.of(400.5),
				Duration.of(240)
			);
			testHub.addHubRoute(hubRoute);
		}

		@Test
		@DisplayName("성공: 허브 경로를 조회한다")
		void getHubRoute_Success() {
			// given
			given(hubRepository.findByIdWithRoutes(testHubId)).willReturn(Optional.of(testHub));

			// when
			HubRouteResult result = hubService.getHubRoute(testHubId, hubRoute.getHubRouteId());

			// then
			assertThat(result).isNotNull();
			verify(hubRepository).findByIdWithRoutes(testHubId);
		}
	}

	@Nested
	@DisplayName("허브 경로 검색 테스트")
	class SearchHubRoutesTest {

		@Test
		@DisplayName("성공: 검색 조건으로 허브 경로 목록을 조회한다")
		void searchHubRoutes_Success() {
			// given
			SearchHubRouteCommand command = SearchHubRouteCommand.of(
				"서울",
				"부산",
				0,
				10,
				"createdAt",
				"DESC"
			);

			Hub arrivalHub = Hub.create(
				"부산 허브",
				Address.of("부산광역시 해운대구 센텀로 456"),
				Coordinate.of(35.1796, 129.0756)
			);

			HubRoute route = HubRoute.create(
				testHub,
				arrivalHub,
				Distance.of(400.5),
				Duration.of(240)
			);

			Page<HubRoute> routePage = new PageImpl<>(List.of(route));
			given(hubRouteQueryRepository.searchHubRoutes(any())).willReturn(routePage);

			// when
			Page<HubRouteResult> result = hubService.searchHubRoutes(command);

			// then
			assertThat(result).isNotEmpty();
			assertThat(result.getContent()).hasSize(1);
			verify(hubRouteQueryRepository).searchHubRoutes(any());
		}
	}

	@Nested
	@DisplayName("허브 경로 수정 테스트")
	class UpdateHubRouteTest {

		private HubRoute hubRoute;

		@BeforeEach
		void setUp() {
			Hub arrivalHub = Hub.create(
				"부산 허브",
				Address.of("부산광역시 해운대구 센텀로 456"),
				Coordinate.of(35.1796, 129.0756)
			);

			hubRoute = HubRoute.create(
				testHub,
				arrivalHub,
				Distance.of(400.5),
				Duration.of(240)
			);
			testHub.addHubRoute(hubRoute);
		}

		@Test
		@DisplayName("성공: 허브 경로를 수정한다")
		void updateHubRoute_Success() {
			// given
			UpdateHubRouteCommand command = UpdateHubRouteCommand.of(
				testHubId,
				hubRoute.getHubRouteId(),
				450.0,
				270
			);

			given(hubRepository.findByIdWithRoutes(testHubId)).willReturn(Optional.of(testHub));

			// when
			HubRouteResult result = hubService.updateHubRoute(testHubId, hubRoute.getHubRouteId(), command);

			// then
			assertThat(result).isNotNull();
			verify(hubRepository).findByIdWithRoutes(testHubId);
		}
	}

	@Nested
	@DisplayName("허브 경로 삭제 테스트")
	class DeleteHubRouteTest {

		private HubRoute hubRoute;

		@BeforeEach
		void setUp() {
			Hub arrivalHub = Hub.create(
				"부산 허브",
				Address.of("부산광역시 해운대구 센텀로 456"),
				Coordinate.of(35.1796, 129.0756)
			);

			hubRoute = HubRoute.create(
				testHub,
				arrivalHub,
				Distance.of(400.5),
				Duration.of(240)
			);
			testHub.addHubRoute(hubRoute);
		}

		@Test
		@DisplayName("성공: 허브 경로를 삭제한다")
		void deleteHubRoute_Success() {
			// given
			given(hubRepository.findByIdWithRoutes(testHubId)).willReturn(Optional.of(testHub));

			// when
			hubService.deleteHubRoute(testHubId, hubRoute.getHubRouteId());

			// then
			verify(hubRepository).findByIdWithRoutes(testHubId);
		}
	}

	@Nested
	@DisplayName("모든 허브 경로 조회 테스트")
	class GetAllRoutesTest {

		@Test
		@DisplayName("성공: 모든 허브 경로를 조회한다")
		void getRoutes_Success() {
			// given
			Hub arrivalHub = Hub.create(
				"부산 허브",
				Address.of("부산광역시 해운대구 센텀로 456"),
				Coordinate.of(35.1796, 129.0756)
			);

			HubRoute route = HubRoute.create(
				testHub,
				arrivalHub,
				Distance.of(400.5),
				Duration.of(240)
			);

			given(hubRouteReadRepository.getRoutes()).willReturn(List.of(route));

			// when
			List<HubRoute> routes = hubService.getRoutes();

			// then
			assertThat(routes).hasSize(1);
			verify(hubRouteReadRepository).getRoutes();
		}
	}

	@Nested
	@DisplayName("특정 허브 경로 조회 테스트")
	class GetRoutesByIdsTest {

		@Test
		@DisplayName("성공: 특정 ID 목록으로 허브 경로를 순서대로 조회한다")
		void getRoutesByIds_Success() {
			// given
			UUID routeId1 = UUID.randomUUID();
			UUID routeId2 = UUID.randomUUID();
			List<UUID> routeIds = List.of(routeId1, routeId2);

			Hub arrivalHub = Hub.create(
				"부산 허브",
				Address.of("부산광역시 해운대구 센텀로 456"),
				Coordinate.of(35.1796, 129.0756)
			);

			HubRoute route1 = HubRoute.create(testHub, arrivalHub, Distance.of(400.5), Duration.of(240));
			HubRoute route2 = HubRoute.create(arrivalHub, testHub, Distance.of(400.5), Duration.of(240));

			given(hubRouteReadRepository.getRoutes(routeIds)).willReturn(List.of(route1, route2));

			// when
			List<HubRoute> routes = hubService.getRoutes(routeIds);

			// then
			assertThat(routes).hasSize(2);
			verify(hubRouteReadRepository).getRoutes(routeIds);
		}

		@Test
		@DisplayName("실패: 일부 경로를 찾을 수 없으면 예외 발생")
		void getRoutesByIds_NotFound() {
			// given
			List<UUID> routeIds = List.of(UUID.randomUUID(), UUID.randomUUID());
			given(hubRouteReadRepository.getRoutes(routeIds)).willReturn(List.of());

			// when & then
			assertThatThrownBy(() -> hubService.getRoutes(routeIds))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("일부 허브 이동정보를 찾을 수 없습니다");
		}
	}

	@Nested
	@DisplayName("재고 생성 테스트")
	class CreateStockTest {

		@Test
		@DisplayName("성공: 재고를 생성한다")
		void createStock_Success() {
			// given
			UUID productId = UUID.randomUUID();
			CreateStockCommand command = CreateStockCommand.of(testHubId, productId, 100);

			given(hubRepository.findById(testHubId)).willReturn(Optional.of(testHub));

			// when
			UUID stockId = hubService.createStock(command);

			// then
			assertThat(stockId).isNotNull();
			verify(hubRepository).findById(testHubId);
		}
	}

	@Nested
	@DisplayName("재고 수정 테스트")
	class UpdateStockTest {

		private Stock stock;

		@BeforeEach
		void setUp() {
			UUID productId = UUID.randomUUID();
			stock = Stock.create(testHub, ProductId.of(productId), 100);
			testHub.addStock(stock);
		}

		@Test
		@DisplayName("성공: 재고 수량을 수정한다")
		void updateStock_Success() {
			// given
			UpdateStockCommand command = UpdateStockCommand.of(testHubId, stock.getStockId(), 200);
			given(hubRepository.findByIdWithStocks(testHubId)).willReturn(Optional.of(testHub));

			// when
			StockResult result = hubService.updateStock(command);

			// then
			assertThat(result).isNotNull();
			verify(hubRepository).findByIdWithStocks(testHubId);
		}
	}

	@Nested
	@DisplayName("재고 차감 테스트")
	class DeductStockTest {

		private Stock stock;
		private UUID productId;

		@BeforeEach
		void setUp() {
			productId = UUID.randomUUID();
			stock = Stock.create(testHub, ProductId.of(productId), 100);
			testHub.addStock(stock);
		}

		@Test
		@DisplayName("성공: 재고를 차감한다")
		void deductStocks_Success() {
			// given
			Map<UUID, Integer> items = Map.of(productId, 50);
			Map<UUID, UUID> productToStockId = Map.of(productId, stock.getStockId());
			DeductStockQuantityCommand command = DeductStockQuantityCommand.of(testHubId, items);

			given(hubRepository.findByIdWithStocks(testHubId)).willReturn(Optional.of(testHub));

			// when
			hubService.deductStocks(command, productToStockId);

			// then
			verify(hubRepository).findByIdWithStocks(testHubId);
		}

		@Test
		@DisplayName("실패: 재고가 부족하면 예외 발생")
		void deductStocks_InsufficientStock() {
			// given
			Map<UUID, Integer> items = Map.of(productId, 150);
			Map<UUID, UUID> productToStockId = Map.of(productId, stock.getStockId());
			DeductStockQuantityCommand command = DeductStockQuantityCommand.of(testHubId, items);

			given(hubRepository.findByIdWithStocks(testHubId)).willReturn(Optional.of(testHub));

			// when & then
			assertThatThrownBy(() -> hubService.deductStocks(command, productToStockId))
				.isInstanceOf(OutOfStockException.class)
				.hasMessageContaining("재고가 부족합니다");
		}
	}

	@Nested
	@DisplayName("재고 복구 테스트")
	class RestoreStockTest {

		private Stock stock;
		private UUID productId;

		@BeforeEach
		void setUp() {
			productId = UUID.randomUUID();
			stock = Stock.create(testHub, ProductId.of(productId), 50);
			testHub.addStock(stock);
		}

		@Test
		@DisplayName("성공: 재고를 복구한다")
		void restoreStocks_Success() {
			// given
			Map<UUID, Integer> items = Map.of(productId, 50);
			Map<UUID, UUID> productToStockId = Map.of(productId, stock.getStockId());
			RestoreStockQuantityCommand command = RestoreStockQuantityCommand.of(testHubId, items);

			given(hubRepository.findByIdWithStocks(testHubId)).willReturn(Optional.of(testHub));

			// when
			hubService.restoreStocks(command, productToStockId);

			// then
			verify(hubRepository).findByIdWithStocks(testHubId);
		}
	}

	@Nested
	@DisplayName("재고 삭제 테스트")
	class DeleteStockTest {

		private Stock stock;

		@BeforeEach
		void setUp() {
			UUID productId = UUID.randomUUID();
			stock = Stock.create(testHub, ProductId.of(productId), 0);
			testHub.addStock(stock);
		}

		@Test
		@DisplayName("성공: 재고를 삭제한다")
		void deleteStock_Success() {
			// given
			DeleteStockCommand command = DeleteStockCommand.of(testHubId, stock.getStockId());
			given(hubRepository.findByIdWithStocks(testHubId)).willReturn(Optional.of(testHub));

			// when
			hubService.deleteStock(command);

			// then
			verify(hubRepository).findByIdWithStocks(testHubId);
		}
	}

	@Nested
	@DisplayName("재고 수량 조회 테스트")
	class GetStockQuantitiesTest {

		@Test
		@DisplayName("성공: 상품 ID 목록으로 재고 수량을 조회한다")
		void getStockQuantities_Success() {
			// given
			UUID productId1 = UUID.randomUUID();
			UUID productId2 = UUID.randomUUID();
			List<UUID> productIds = List.of(productId1, productId2);

			Stock stock1 = Stock.create(testHub, ProductId.of(productId1), 100);
			Stock stock2 = Stock.create(testHub, ProductId.of(productId2), 200);

			given(stockReadRepository.getStocks(productIds)).willReturn(List.of(stock1, stock2));

			// when
			Map<UUID, Integer> quantities = hubService.getStockQuantities(productIds);

			// then
			assertThat(quantities).hasSize(2);
			assertThat(quantities.get(productId1)).isEqualTo(100);
			assertThat(quantities.get(productId2)).isEqualTo(200);
			verify(stockReadRepository).getStocks(productIds);
		}

		@Test
		@DisplayName("실패: 일부 재고를 찾을 수 없으면 예외 발생")
		void getStockQuantities_NotFound() {
			// given
			List<UUID> productIds = List.of(UUID.randomUUID(), UUID.randomUUID());
			given(stockReadRepository.getStocks(productIds)).willReturn(List.of());

			// when & then
			assertThatThrownBy(() -> hubService.getStockQuantities(productIds))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("일부 재고를 찾을 수 없습니다");
		}
	}
}