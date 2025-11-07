package com.delivery_signal.eureka.client.hub.domain.repository;

import org.springframework.data.domain.Page;

import com.delivery_signal.eureka.client.hub.domain.model.Hub;
import com.delivery_signal.eureka.client.hub.domain.mapper.HubSearchCondition;

public interface HubQueryRepository {

	/**
	 * 허브 검색
	 * @param condition 검색 조건
	 * @return 허브 목록
	 */
	Page<Hub> searchHubs(HubSearchCondition condition);
}