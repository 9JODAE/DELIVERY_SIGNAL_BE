package com.delivery_signal.eureka.client.hub.presentation.dto.request;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

public record RestoreStockQuantityRequest(
	@NotEmpty(message = "상품 목록은 비어 있을 수 없습니다.")
	Map<UUID, Integer> products
) {}
