package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delivery_signal.eureka.client.hub.domain.entity.Stock;

public interface JpaStockRepository extends JpaRepository<Stock, UUID> {

	@Query("""
	SELECT s FROM Stock s
	WHERE s.productId.value IN :productIds
	""")
	List<Stock> findByProductIdIn(List<UUID> productIds);
}
