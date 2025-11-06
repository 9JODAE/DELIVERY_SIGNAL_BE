package com.delivery_signal.eureka.client.hub.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery_signal.eureka.client.hub.application.command.CreateHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.SearchHubCommand;
import com.delivery_signal.eureka.client.hub.application.command.UpdateHubCommand;
import com.delivery_signal.eureka.client.hub.application.dto.HubResult;
import com.delivery_signal.eureka.client.hub.domain.model.Hub;
import com.delivery_signal.eureka.client.hub.domain.repository.HubQueryRepository;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRepository;
import com.delivery_signal.eureka.client.hub.domain.vo.Address;
import com.delivery_signal.eureka.client.hub.domain.vo.Coordinate;
import com.delivery_signal.eureka.client.hub.domain.vo.HubSearchCondition;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HubService {

	private final HubRepository hubRepository;
	private final HubQueryRepository hubQueryRepository;

	public UUID createHub(CreateHubCommand command) {
		Address address = Address.of(command.address());
		Coordinate coordinate = Coordinate.of(command.latitude(), command.longitude());
		Hub savedHub = hubRepository.save(
			Hub.create(command.name(), address, coordinate));
		return savedHub.getHubId();
	}

	@Transactional(readOnly = true)
	public Page<HubResult> searchHubs(SearchHubCommand command) {
		HubSearchCondition condition = new HubSearchCondition(
			command.name(),
			command.address(),
			command.page(),
			command.size(),
			command.sortBy(),
			command.direction()
		);

		return hubQueryRepository.searchHubs(condition)
			.map(HubResult::from);
	}

	@Transactional(readOnly = true)
	public HubResult getHub(UUID hubId) {
		Hub hub = getHubOrThrow(hubId);
		return HubResult.from(hub);
	}

	public HubResult updateHub(UpdateHubCommand command) {
	    Hub hub = getHubOrThrow(command.hubId());
		Address address = Address.of(command.address());
		Coordinate coordinate = Coordinate.of(command.latitude(), command.longitude());
	    hub.update(command.name(), address, coordinate);
		return HubResult.from(hub);
	}

	public void deleteHub(UUID hubId) {
		Hub hub = getHubOrThrow(hubId);
		hub.softDelete(1L); // TODO 유저 서비스 개발 완료 시 변경
	}

	private Hub getHubOrThrow(UUID hubId) {
		return hubRepository.findById(hubId)
			.orElseThrow(() -> new IllegalArgumentException("허브를 찾을 수 없습니다. hubId=" + hubId));
	}
}
