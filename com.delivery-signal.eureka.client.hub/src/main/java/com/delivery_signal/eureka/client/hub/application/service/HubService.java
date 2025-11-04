package com.delivery_signal.eureka.client.hub.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_signal.eureka.client.hub.application.dto.HubDetailResponse;
import com.delivery_signal.eureka.client.hub.domain.model.Hub;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRepository;
import com.delivery_signal.eureka.client.hub.presentation.request.CreateHubRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HubService {

	private final HubRepository hubRepository;

	public HubDetailResponse createHub(CreateHubRequest request) {
		Hub hub = hubRepository.save(
			Hub.create(request.getName(), request.getAddress(), request.getLatitude(), request.getLongitude()));

		return HubDetailResponse.from(hub);
	}
}
