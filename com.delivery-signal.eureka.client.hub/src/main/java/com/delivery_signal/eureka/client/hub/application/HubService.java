package com.delivery_signal.eureka.client.hub.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_signal.eureka.client.hub.application.command.CreateHubCommand;
import com.delivery_signal.eureka.client.hub.domain.model.Hub;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRepository;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubCreateResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HubService {

	private final HubRepository hubRepository;

	public HubCreateResponse createHub(CreateHubCommand command) {
		Hub savedHub = hubRepository.save(
			Hub.create(command.name(), command.address(), command.latitude(), command.longitude()));
		return HubCreateResponse.of(savedHub.getHubId());
	}
}
