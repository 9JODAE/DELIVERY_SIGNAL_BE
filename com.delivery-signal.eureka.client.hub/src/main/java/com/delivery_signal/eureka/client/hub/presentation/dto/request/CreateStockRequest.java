package com.delivery_signal.eureka.client.hub.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 재고 생성 요청 DTO
 */
public record CreateStockRequest(
	@NotNull(message = "상품 ID는 필수 입력 항목입니다.")
	UUID productId,

	@Min(value = 1, message = "수량은 1 이상이어야 합니다.")
	@NotNull(message = "수량은 필수 입력 항목입니다.")
	Integer quantity
) {}
