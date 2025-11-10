package com.delivery_signal.eureka.client.hub.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_signal.eureka.client.hub.application.HubService;
import com.delivery_signal.eureka.client.hub.application.command.CreateHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.CreateHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.CreateStockCommand;
import com.delivery_signal.eureka.client.hub.application.command.DeleteStockCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchStockCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateStockCommand;
import com.delivery_signal.eureka.client.hub.application.facade.StockFacade;
import com.delivery_signal.eureka.client.hub.application.facade.StockSearchFacade;
import com.delivery_signal.eureka.client.hub.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.CreateHubRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.CreateHubRouteRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.CreateStockRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.UpdateHubRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.UpdateHubRouteRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.UpdateStockRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.CreateHubResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.CreateHubRouteResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.CreateStockResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubDetailResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubRouteDetailResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubRouteResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.StockDetailResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.UpdateStockResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hubs")
public class HubController {

	private final HubService hubService;
	private final StockFacade stockFacade;
	private final StockSearchFacade stockSearchFacade;

	/**
	 * 허브 생성
	 * POST /v1/hubs
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<CreateHubResponse>> createHub(@Valid @RequestBody CreateHubRequest request) {
		CreateHubCommand command = CreateHubCommand.of(
			request.name(),
			request.address(),
			request.latitude(),
			request.longitude()
		);
		CreateHubResponse response = CreateHubResponse.of(hubService.createHub(command));
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
	}

	/**
	 * 허브 목록 조회
	 * GET /v1/hubs
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<Page<HubResponse>>> searchHubs(
		@RequestParam(required = false) String name,
		@RequestParam(required = false) String address,
		@RequestParam(required = false) Integer page,
		@RequestParam(required = false) Integer size,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) String direction
	) {
		SearchHubCommand command = SearchHubCommand.of(name, address, page, size, sortBy, direction);
		Page<HubResponse> response = hubService.searchHubs(command)
			.map(HubResponse::from);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	/**
	 * 허브 상세 조회
	 * GET /v1/hubs/{hubId}
	 */
	@GetMapping("/{hubId}")
	public ResponseEntity<ApiResponse<HubDetailResponse>> getHub(@PathVariable UUID hubId) {
		HubDetailResponse response = HubDetailResponse.from(hubService.getHub(hubId));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	/**
	 * 허브 수정
	 * PUT /v1/hubs/{hubId}
	 */
	@PutMapping("/{hubId}")
	public ResponseEntity<ApiResponse<HubDetailResponse>> updateHub(
		@PathVariable UUID hubId,
		@Valid @RequestBody UpdateHubRequest request
	) {
		UpdateHubCommand command = UpdateHubCommand.of(
			hubId,
			request.name(),
			request.address(),
			request.latitude(),
			request.longitude()
		);
		HubDetailResponse response = HubDetailResponse.from(hubService.updateHub(command));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	/**
	 * 허브 삭제
	 * DELETE /v1/hubs/{hubId}
	 */
	@DeleteMapping("/{hubId}")
	public ResponseEntity<ApiResponse<Void>> deleteHub(@PathVariable UUID hubId) {
		hubService.deleteHub(hubId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("허브가 삭제되었습니다."));
	}



	/**
	 * 허브 이동정보 생성
	 * POST /v1/hubs/{hubId}/routes
	 */
	@PostMapping("/{hubId}/routes")
	public ResponseEntity<ApiResponse<CreateHubRouteResponse>> createHubRoute(
		@PathVariable UUID hubId,
		@Valid @RequestBody CreateHubRouteRequest request
	) {
		CreateHubRouteCommand command = CreateHubRouteCommand.of(
			hubId,
			request.arrivalHubId(),
			request.distance(),
			request.transitTime()
		);
		CreateHubRouteResponse response = CreateHubRouteResponse.of(hubService.createHubRoute(command));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	/**
	 * 허브 이동정보 검색
	 * GET /v1/hubs/routes
	 */
	@GetMapping("/routes")
	public ResponseEntity<ApiResponse<Page<HubRouteResponse>>> searchHubRoutes(
		@RequestParam(required = false) String departureHubName,
		@RequestParam(required = false) String arrivalHubName,
		@RequestParam(required = false) Integer page,
		@RequestParam(required = false) Integer size,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) String direction
	) {
		SearchHubRouteCommand command = SearchHubRouteCommand.of(
			departureHubName, arrivalHubName, page, size, sortBy, direction
		);
		Page<HubRouteResponse> response = hubService.searchHubRoutes(command)
			.map(HubRouteResponse::from);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	/**
	 * 허브 이동정보 조회
	 * GET /v1/hubs/{hubId}/routes/{hubRouteId}
	 */
	@GetMapping("/{hubId}/routes/{hubRouteId}")
	public ResponseEntity<ApiResponse<HubRouteDetailResponse>> getHubRoute(
		@PathVariable UUID hubId,
		@PathVariable UUID hubRouteId
	) {
		HubRouteDetailResponse response = HubRouteDetailResponse.from(hubService.getHubRoute(hubId, hubRouteId));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	/**
	 * 허브 이동정보 수정
	 * PUT /v1/hubs/{hubId}/routes/{hubRouteId}
	 */
	@PutMapping("/{hubId}/routes/{hubRouteId}")
	public ResponseEntity<ApiResponse<HubRouteDetailResponse>> updateHubRoute(
		@PathVariable UUID hubId,
		@PathVariable UUID hubRouteId,
		@Valid @RequestBody UpdateHubRouteRequest request
	) {
		UpdateHubRouteCommand command = UpdateHubRouteCommand.of(
			hubId,
			hubRouteId,
			request.distance(),
			request.transitTime()
		);
		HubRouteDetailResponse response = HubRouteDetailResponse.from(
			hubService.updateHubRoute(hubId, hubRouteId, command)
		);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	/**
	 * 허브 이동정보 삭제
	 * DELETE /v1/hubs/{hubId}/routes/{hubRouteId}
	 */
	@DeleteMapping("/{hubId}/routes/{hubRouteId}")
	public ResponseEntity<ApiResponse<Void>> deleteHubRoute(
		@PathVariable UUID hubId,
		@PathVariable UUID hubRouteId
	) {
		hubService.deleteHubRoute(hubId, hubRouteId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("허브 이동정보가 삭제되었습니다."));
	}



	/**
	 * 허브 재고 생성
	 * POST /v1/hubs/{hubId}/stocks
	 */
	@PostMapping("/{hubId}/stocks")
	public ResponseEntity<ApiResponse<CreateStockResponse>> createStock(
		@PathVariable UUID hubId,
		@Valid @RequestBody CreateStockRequest request
	) {
		CreateStockCommand command = CreateStockCommand.of(
			hubId,
			request.productId(),
			request.quantity()
		);
		CreateStockResponse response = CreateStockResponse.of(hubService.createStock(command));
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
	}

	/**
	 * 허브 재고 검색
	 * GET /v1/hubs/stocks
	 */
	@GetMapping("/stocks")
	public ResponseEntity<ApiResponse<Page<StockDetailResponse>>> searchStocks(
		@RequestParam(required = false) String productName,
		@RequestParam(required = false) Integer page,
		@RequestParam(required = false) Integer size,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) String direction
	) {
		// TODO 유저 서비스 개발 완료 시, hubId 추가
		UUID hubId = UUID.fromString("2965b14e-21df-4b15-b5cf-cfaf6d6bee8f");
		SearchStockCommand command = SearchStockCommand.of(hubId, productName, page, size, sortBy, direction);
		Page<StockDetailResponse> response = stockSearchFacade.searchStocks(command)
			.map(StockDetailResponse::from);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	/**
	 * 허브 재고 수정
	 * PUT /v1/hubs/{hubId}/stocks/{stockId}
	 */
	@PutMapping("/{hubId}/stocks/{stockId}")
	public ResponseEntity<ApiResponse<UpdateStockResponse>> updateStock(
		@PathVariable UUID hubId,
		@PathVariable UUID stockId,
		@Valid @RequestBody UpdateStockRequest request
	) {
		log.info("request received: hubId={}, stockId={}, quantity={}",
			hubId, stockId, request.quantity());
		UpdateStockCommand command = UpdateStockCommand.of(hubId, stockId, request.quantity());
		UpdateStockResponse response = UpdateStockResponse.from(stockFacade.updateStock(command));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	/**
	 * 허브 재고 삭제
	 * DELETE /v1/hubs/{hubId}/stocks/{stockId}
	 */
	@DeleteMapping("/{hubId}/stocks/{stockId}")
	public ResponseEntity<ApiResponse<Void>> deleteStock(
		@PathVariable UUID hubId,
		@PathVariable UUID stockId
	) {
		DeleteStockCommand command = DeleteStockCommand.of(hubId, stockId);
		hubService.deleteStock(command);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("재고가 삭제되었습니다."));
	}
}