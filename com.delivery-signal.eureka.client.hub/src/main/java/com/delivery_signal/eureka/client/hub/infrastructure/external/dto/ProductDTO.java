package com.delivery_signal.eureka.client.hub.infrastructure.external.dto;

import java.math.BigDecimal;

public record ProductDTO(
	String productName,
	BigDecimal price
) {}
