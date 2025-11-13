package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delivery_signal.eureka.client.hub.domain.entity.HubRoute;

public interface JpaHubRouteRepository extends JpaRepository<HubRoute, UUID> {

	@Query("""
	SELECT r FROM HubRoute r
	JOIN FETCH r.departureHub
	JOIN FETCH r.arrivalHub
	WHERE r.hubRouteId IN :routeIds
	""")
	List<HubRoute> findAllByIds(List<UUID> routeIds);

}
