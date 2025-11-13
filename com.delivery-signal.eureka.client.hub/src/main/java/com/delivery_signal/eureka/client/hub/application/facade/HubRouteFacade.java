package com.delivery_signal.eureka.client.hub.application.facade;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.delivery_signal.eureka.client.hub.application.HubService;
import com.delivery_signal.eureka.client.hub.application.command.GetHubRouteCommand;
import com.delivery_signal.eureka.client.hub.application.dto.HubRouteResult;
import com.delivery_signal.eureka.client.hub.domain.model.Hub;
import com.delivery_signal.eureka.client.hub.domain.model.HubRoute;
import com.delivery_signal.eureka.client.hub.domain.service.HubPathService;
import com.delivery_signal.eureka.client.hub.common.annotation.Cacheable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HubRouteFacade {

	private final HubService hubService;
	private final HubPathService hubPathService;

	@Cacheable(
		value = "hubRoutes",
		key = "#command.departureHubId() + '-' + #command.arrivalHubId()"
	)
	public List<HubRouteResult> findShortestPath(GetHubRouteCommand command) {
		Hub departureHub = getHub(command.departureHubId());
		Hub arrivalHub = getHub(command.arrivalHubId());

		List<UUID> orderedRoutes = calculateShortestPath(departureHub, arrivalHub);
		List<HubRoute> result = hubService.getRoutes(orderedRoutes);

		return result.stream()
			.map(HubRouteResult::from)
			.toList();
	}

	private Hub getHub(UUID hubId) {
		return hubService.getHubOrThrow(hubId);
	}

	private List<UUID> calculateShortestPath(Hub departure, Hub arrival) {
		List<HubRoute> allRoutes = hubService.getRoutes();
		return hubPathService.findShortestPath(allRoutes, departure.getHubId(), arrival.getHubId());
	}
}