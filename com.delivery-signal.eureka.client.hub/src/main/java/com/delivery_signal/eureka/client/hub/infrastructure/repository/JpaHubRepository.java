package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delivery_signal.eureka.client.hub.domain.model.Hub;

import io.lettuce.core.dynamic.annotation.Param;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {

	@Query("""
	SELECT h FROM Hub h
	LEFT JOIN FETCH h.hubRoutes
	WHERE h.hubId = :hubId
	""")
	Optional<Hub> findByIdWithRoutes(@Param("hubId") UUID hubId);

	@Query("""
	SELECT h FROM Hub h
	LEFT JOIN FETCH h.stocks
	WHERE h.hubId = :hubId
	""")
	Optional<Hub> findByIdWithStocks(@Param("hubId") UUID hubId);
}
