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
import com.delivery_signal.eureka.client.hub.application.command.SearchHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateHubCommand;
import com.delivery_signal.eureka.client.hub.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.CreateHubRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.CreateHubRouteRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.UpdateHubRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.CreateHubResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.CreateHubRouteResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubDetailResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubRouteDetailResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubRouteResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hubs")
public class HubController {

	private final HubService hubService;

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
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("허브가 삭제되었습니다."));
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

	@GetMapping("/{hubId}/routes/{hubRouteId}")
	public ResponseEntity<ApiResponse<HubRouteDetailResponse>> getHubRoute(
		@PathVariable UUID hubId,
		@PathVariable UUID hubRouteId
	) {
		HubRouteDetailResponse response = HubRouteDetailResponse.from(hubService.getHubRoute(hubId, hubRouteId));
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}


}
