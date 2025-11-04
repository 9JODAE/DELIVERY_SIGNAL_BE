package com.delivery_signal.eureka.client.hub.presentation.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery_signal.eureka.client.hub.application.dto.HubDetailResponse;
import com.delivery_signal.eureka.client.hub.application.service.HubService;
import com.delivery_signal.eureka.client.hub.presentation.request.CreateHubRequest;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hubs")
public class HubController {

	private final HubService hubService;

	@PostMapping
	public ResponseEntity<HubDetailResponse> createHub(@Valid @RequestBody CreateHubRequest request) {
		return ResponseEntity.ok(hubService.createHub(request));
	}
}
