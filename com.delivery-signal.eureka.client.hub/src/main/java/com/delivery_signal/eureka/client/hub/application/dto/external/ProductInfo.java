package com.delivery_signal.eureka.client.hub.application.dto.external;

import java.math.BigDecimal;

public record ProductInfo(
	String productName,
	BigDecimal price
) {}
