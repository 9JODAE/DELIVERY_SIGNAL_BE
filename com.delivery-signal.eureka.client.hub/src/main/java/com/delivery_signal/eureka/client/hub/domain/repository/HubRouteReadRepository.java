package com.delivery_signal.eureka.client.hub.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.delivery_signal.eureka.client.hub.domain.model.HubRoute;

public interface HubRouteReadRepository {

	/**
	 * 모든 허브 경로 조회
	 * @return 허브 경로 리스트
	 */
	List<HubRoute> getRoutes();

	List<HubRoute> getRoutes(List<UUID> hubRouteIds);
}
