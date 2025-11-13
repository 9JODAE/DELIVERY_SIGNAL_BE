package com.delivery_signal.eureka.client.hub.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.delivery_signal.eureka.client.hub.domain.entity.Hub;

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

	/**
	 * 허브 및 경로 조회 (Fetch Join)
	 * @param hubId 허브 아이디
	 * @return 조회된 허브 (경로 포함)
	 */
	Optional<Hub> findByIdWithRoutes(UUID hubId);

	/**
	 * 허브 및 재고 조회 (Fetch Join)
	 * @param hubId 허브 아이디
	 * @return 조회된 허브 (재고 포함)
	 */
	Optional<Hub> findByIdWithStocks(UUID hubId);

	/**
	 * 허브 존재 여부 확인
	 * @param hubId 허브 아이디
	 * @return 허브 존재 여부
	 */
	boolean exists(UUID hubId);
}
