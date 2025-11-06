package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.delivery_signal.eureka.client.hub.domain.model.Hub;
import com.delivery_signal.eureka.client.hub.domain.repository.HubRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubRepositoryAdapter implements HubRepository {

	private final JpaHubRepository jpaHubRepository;

	@Override
	public Hub save(Hub hub) {
		return jpaHubRepository.save(hub);
	}

	@Override
	public Optional<Hub> findById(UUID hubId) {
		return jpaHubRepository.findById(hubId);
	}
}
