package com.delivery_signal.eureka.client.hub.infrastructure.external.mapper;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.delivery_signal.eureka.client.hub.application.dto.external.ProductInfo;
import com.delivery_signal.eureka.client.hub.infrastructure.annotation.Mapper;
import com.delivery_signal.eureka.client.hub.infrastructure.external.dto.ProductDTO;

@Mapper
public class ProductMapper {

	public ProductInfo toProductInfo(ProductDTO productDTO) {
		return new ProductInfo(
			productDTO.productName(),
			productDTO.price()
		);
	}

	public Map<UUID, ProductInfo> toProductInfoMap(Map<UUID, ProductDTO> productDTOs) {
		return productDTOs.entrySet()
			.stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> toProductInfo(e.getValue())
			)
		);
	}
}
