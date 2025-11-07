package com.delivery_signal.eureka.client.hub.presentation.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 허브 수정 요청 DTO
 */
public record UpdateHubRequest(
	@Size(min = 5, max = 20, message = "이름은 5자 이상 20자 이하이어야 합니다.")
	@NotBlank(message = "허브 이름은 필수 입력 항목입니다.")
	String name,

	@Size(min = 12, max = 100, message = "주소는 12자 이상 100자 이하이어야 합니다.")
	@NotBlank(message = "주소는 필수 입력 항목입니다.")
	String address,

	@DecimalMin(value = "33", message = "위도는 33 이상이어야 합니다.")
	@DecimalMax(value = "39", message = "위도는 39 이하이어야 합니다.")
	@NotNull(message = "위도는 필수 입력 항목입니다.")
	Double latitude,

	@DecimalMin(value = "124", message = "경도는 124 이상이어야 합니다.")
	@DecimalMax(value = "132", message = "경도는 132 이하이어야 합니다.")
	@NotNull(message = "경도는 필수 입력 항목입니다.")
	Double longitude
) {}