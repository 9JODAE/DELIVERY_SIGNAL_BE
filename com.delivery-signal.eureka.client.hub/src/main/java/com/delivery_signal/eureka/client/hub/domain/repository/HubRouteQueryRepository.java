package com.delivery_signal.eureka.client.hub.domain.repository;

import org.springframework.data.domain.Page;

import com.delivery_signal.eureka.client.hub.domain.model.HubRoute;
import com.delivery_signal.eureka.client.hub.domain.mapper.HubRouteSearchCondition;

public interface HubRouteQueryRepository {

	/**
	 * 허브 이동정보 검색
	 * @param condition 검색 조건
	 * @return 허브 경로 목록
	 */
	Page<HubRoute> searchHubRoutes(HubRouteSearchCondition condition);
}
