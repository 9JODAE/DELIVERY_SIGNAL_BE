package com.delivery_signal.eureka.client.hub.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
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
import com.delivery_signal.eureka.client.hub.application.command.SearchHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateHubCommand;
import com.delivery_signal.eureka.client.hub.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.CreateHubRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.UpdateHubRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubCreateResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubDetailResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
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
	public ResponseEntity<ApiResponse<HubCreateResponse>> createHub(@Valid @RequestBody CreateHubRequest request) {
		CreateHubCommand command = CreateHubCommand.from(request);
		HubCreateResponse response = HubCreateResponse.of(hubService.createHub(command));
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
		@RequestParam(required = false) Sort.Direction direction
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
		UpdateHubCommand command = UpdateHubCommand.of(hubId, request);
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
}
