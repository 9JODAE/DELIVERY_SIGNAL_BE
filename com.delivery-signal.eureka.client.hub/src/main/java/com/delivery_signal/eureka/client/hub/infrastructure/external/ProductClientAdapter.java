package com.delivery_signal.eureka.client.hub.infrastructure.external;

import java.util.Map;
import java.util.UUID;

import com.delivery_signal.eureka.client.hub.application.dto.external.ProductInfo;
import com.delivery_signal.eureka.client.hub.application.port.ProductClient;
import com.delivery_signal.eureka.client.hub.infrastructure.annotation.ExternalAdapter;
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
		return productFeignClient.exists(productId);
	}

	@Override
	public Map<UUID, ProductInfo> searchProducts(UUID hubId, String productName) {
		Map<UUID, ProductDTO> productDTOs = productFeignClient.searchProducts(hubId, productName);
		return productMapper.toProductInfoMap(productDTOs);
	}
}
