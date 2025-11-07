package com.delivery_signal.eureka.client.hub.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 허브 경로 생성 요청 DTO
 */
public record CreateHubRouteRequest(
	@NotNull(message = "도착 허브 ID는 필수 입력 항목입니다.")
	UUID arrivalHubId,

	@DecimalMin(value = "0.0", message = "거리는 0 이상이어야 합니다.")
	@DecimalMax(value = "10000.0", message = "거리는 100000 이하이어야 합니다.")
	@NotNull(message = "거리는 필수 입력 항목입니다.")
	Double distance,

	@Min(value = 0, message = "소요 시간은 0 이상이어야 합니다.")
	@NotNull(message = "소요 시간은 필수 입력 항목입니다.")
	Integer transitTime
) {}