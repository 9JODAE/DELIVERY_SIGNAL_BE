package com.delivery_signal.eureka.client.hub.presentation.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_signal.eureka.client.hub.application.HubService;
import com.delivery_signal.eureka.client.hub.application.command.DeductStockQuantityCommand;
import com.delivery_signal.eureka.client.hub.application.command.GetHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.RestoreStockQuantityCommand;
import com.delivery_signal.eureka.client.hub.application.facade.HubRouteFacade;
import com.delivery_signal.eureka.client.hub.application.facade.StockFacade;
import com.delivery_signal.eureka.client.hub.common.api.ApiResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.DeductStockQuantityRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.GetStockQuantitiesRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.RestoreStockQuantityRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.PathResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/open-api/v1")
public class HubOpenApiController {

	private final HubService hubService;
	private final StockFacade stockFacade;
	private final HubRouteFacade hubRouteFacade;

	@PostMapping("/stocks")
	public Map<UUID, Integer> getStockQuantities(
		@Valid @RequestBody GetStockQuantitiesRequest request
	) {
		return hubService.getStockQuantities(request.productIds());
	};

	@PostMapping("/hubs/{hubId}/stocks/deduct")
	public ResponseEntity<ApiResponse<Void>> deductStocks(
		@PathVariable UUID hubId,
		@Valid @RequestBody DeductStockQuantityRequest request
	) {
		DeductStockQuantityCommand command = DeductStockQuantityCommand.of(hubId, request.items());
		stockFacade.deductStocks(command);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("재고 차감이 완료되었습니다."));
	}

	@PostMapping("/hubs/{hubId}/stocks/restore")
	public ResponseEntity<ApiResponse<Void>> restoreStocks(
		@PathVariable UUID hubId,
		@Valid @RequestBody RestoreStockQuantityRequest request
	) {
		RestoreStockQuantityCommand command = RestoreStockQuantityCommand.of(hubId, request.items());
		stockFacade.restoreStocks(command);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("재고 복구가 완료되었습니다."));
	}

	@GetMapping("/hubs/{hubId}")
	public boolean existsHub(@PathVariable UUID hubId) {
		return hubService.existsHub(hubId);
	}

	@GetMapping("/routes")
	public ResponseEntity<ApiResponse<List<PathResponse>>> getRoutes(
		@RequestParam("departure") UUID departureHubId,
		@RequestParam("arrival") UUID arrivalHubId
	) {
		GetHubRouteCommand command = GetHubRouteCommand.of(departureHubId, arrivalHubId);
		List<PathResponse> response = hubRouteFacade.findShortestPath(command).stream()
			.map(PathResponse::from)
			.toList();

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}
}
