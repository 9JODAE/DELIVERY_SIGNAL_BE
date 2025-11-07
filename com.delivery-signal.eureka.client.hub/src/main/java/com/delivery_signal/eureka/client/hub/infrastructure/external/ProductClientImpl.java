package com.delivery_signal.eureka.client.hub.infrastructure.external;

import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.service.ProductClient;
import com.delivery_signal.eureka.client.hub.infrastructure.annotation.ExternalAdapter;

import lombok.RequiredArgsConstructor;

@ExternalAdapter
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

	private final ProductFeign productFeign;

	@Override
	public boolean existsProduct(UUID productId) {
		return productFeign.exists(productId);
	}
}
