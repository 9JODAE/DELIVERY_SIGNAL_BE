package com.delivery_signal.eureka.client.hub.domain.repository;

import com.delivery_signal.eureka.client.hub.domain.model.Hub;

public interface HubRepository {

	/**
	 * 허브 저장
	 * @param hub 저장할 허브
	 * @return 저장된 허브
	 */
	Hub save(Hub hub);
}
