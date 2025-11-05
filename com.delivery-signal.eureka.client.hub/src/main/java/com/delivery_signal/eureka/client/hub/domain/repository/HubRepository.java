package com.delivery_signal.eureka.client.hub.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.delivery_signal.eureka.client.hub.domain.model.Hub;

public interface HubRepository {

	/**
	 * 허브 저장
	 * @param hub 저장할 허브
	 * @return 저장된 허브
	 */
	Hub save(Hub hub);

	/**
	 * 허브 조회
	 * @param hubId 허브 아이디
	 * @return 조회된 허브
	 */
	Optional<Hub> findById(UUID hubId);
}
