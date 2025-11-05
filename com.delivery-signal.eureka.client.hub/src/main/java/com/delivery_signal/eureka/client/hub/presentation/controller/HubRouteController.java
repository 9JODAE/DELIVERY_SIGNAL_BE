package com.delivery_signal.eureka.client.hub.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_signal.eureka.client.hub.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.GetHubRouteRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.GetHubRouteResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hub-routes")
public class HubRouteController {

	@GetMapping
	public ResponseEntity<ApiResponse<List<GetHubRouteResponse>>> getHubRoutes(
		@RequestBody GetHubRouteRequest request
	) {
		List<GetHubRouteResponse> routes = List.of(
			// 서울 → 경기남부
			new GetHubRouteResponse("서울", "경기남부", 76, 88),
			// 경기남부 → 대구
			new GetHubRouteResponse("경기남부", "대구", 139, 158),
			// 대구 → 부산
			new GetHubRouteResponse("대구", "부산", 84, 109)
		);

		ApiResponse<List<GetHubRouteResponse>> response = ApiResponse.success(routes);
		return ResponseEntity.ok(response);
	}

}
