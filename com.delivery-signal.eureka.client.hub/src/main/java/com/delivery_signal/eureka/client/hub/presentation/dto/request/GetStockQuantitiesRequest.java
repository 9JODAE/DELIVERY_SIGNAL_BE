package com.delivery_signal.eureka.client.hub.presentation.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

public record GetStockQuantitiesRequest(
	@NotEmpty(message = "상품 ID 목록은 필수 입력 항목입니다.")
	List<UUID> productIds
) {}
