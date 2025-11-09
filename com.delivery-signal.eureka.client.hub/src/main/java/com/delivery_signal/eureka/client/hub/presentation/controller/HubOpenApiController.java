package com.delivery_signal.eureka.client.hub.presentation.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_signal.eureka.client.hub.application.HubService;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.GetStockQuantitiesRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/open-api/v1")
public class HubOpenApiController {

	private final HubService hubService;

	@PostMapping("/stocks")
	Map<UUID, Integer> getStockQuantities(
		@Valid @RequestBody GetStockQuantitiesRequest request
	) {
		return hubService.getStockQuantities(request.productIds());
	};
}
