package com.delivery_signal.eureka.client.hub.infrastructure.external;

import java.util.Map;
import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.external.ProductInfo;
import com.delivery_signal.eureka.client.hub.application.port.ProductClient;
import com.delivery_signal.eureka.client.hub.common.annotation.ExternalAdapter;
import com.delivery_signal.eureka.client.hub.common.api.ApiResponse;
import com.delivery_signal.eureka.client.hub.infrastructure.external.dto.ProductDTO;
import com.delivery_signal.eureka.client.hub.infrastructure.external.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@ExternalAdapter
@RequiredArgsConstructor
public class ProductClientAdapter implements ProductClient {

	private final ProductFeignClient productFeignClient;
	private final ProductMapper productMapper;

	@Override
	public boolean exists(UUID productId) {
		ApiResponse<Boolean> response = productFeignClient.exists(productId);
		return response.data();
	}

	@Override
	public Map<UUID, ProductInfo> searchProducts(UUID hubId, String productName) {
		ApiResponse<Map<UUID, ProductDTO>> response = productFeignClient.searchProducts(hubId, productName);
		return productMapper.toProductInfoMap(response.data());
	}
}
