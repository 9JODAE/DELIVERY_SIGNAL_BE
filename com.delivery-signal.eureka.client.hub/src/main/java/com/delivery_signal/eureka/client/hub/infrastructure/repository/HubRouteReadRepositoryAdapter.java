package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.delivery_signal.eureka.client.hub.domain.entity.HubRoute;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRouteReadRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubRouteReadRepositoryAdapter implements HubRouteReadRepository {

	private final JpaHubRouteRepository jpaHubRouteRepository;

	@Override
	public List<HubRoute> getRoutes() {
		return jpaHubRouteRepository.findAll();
	}

	@Override
	public List<HubRoute> getRoutes(List<UUID> routeIds) {
		return jpaHubRouteRepository.findAllByIds(routeIds);
	}
}
