package com.delivery_signal.eureka.client.hub.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_signal.eureka.client.hub.application.command.CreateHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchHubCommand;
import com.delivery_signal.eureka.client.hub.application.dto.HubResult;
import com.delivery_signal.eureka.client.hub.domain.model.Hub;
import com.delivery_signal.eureka.client.hub.domain.repository.HubQueryRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRepository;
import com.delivery_signal.eureka.client.hub.presentation.dto.response.HubCreateResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HubService {

	private final HubRepository hubRepository;
	private final HubQueryRepository hubQueryRepository;

	public UUID createHub(CreateHubCommand command) {
		Hub savedHub = hubRepository.save(
			Hub.create(command.name(), command.address(), command.latitude(), command.longitude()));
		return savedHub.getHubId();
	}

	@Transactional(readOnly = true)
	public Page<HubResult> searchHubs(SearchHubCommand command) {
		return hubQueryRepository.searchHubs(command)
			.map(HubResult::from);
	}
}
