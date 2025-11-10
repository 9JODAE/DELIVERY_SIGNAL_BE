package com.delivery_signal.eureka.client.hub.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateStockRequest(
	@NotNull(message = "수량은 필수 입력 항목입니다.")
	@Min(value = 1, message = "수량은 1 이상이어야 합니다.")
	Integer quantity
) {}
