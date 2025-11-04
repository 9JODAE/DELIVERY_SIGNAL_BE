package com.delivery_signal.eureka.client.hub.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_signal.eureka.client.hub.application.HubService;
import com.delivery_signal.eureka.client.hub.application.command.CreateHubCommand;
import com.delivery_signal.eureka.client.hub.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.hub.presentation.dto.request.CreateHubRequest;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubCreateResponse;

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
		HubCreateResponse response = hubService.createHub(command);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
	}
}
