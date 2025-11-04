package com.delivery_signal.eureka.client.hub.presentation.dto.request;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 허브 생성 요청 DTO
 */
public record CreateHubRequest(
	@NotBlank(message = "이름은 필수 입력 항목입니다.")
	String name,

	@NotBlank(message = "주소는 필수 입력 항목입니다.")
	String address,

	@Range(min = 33, max = 39, message = "위도는 33 ~ 39 사이여야 합니다.")
	@NotNull(message = "위도는 필수 입력 항목입니다.")
	Double latitude,

	@Range(min = 124, max = 132, message = "경도는 124 ~ 132 사이여야 합니다.")
	@NotNull(message = "경도는 필수 입력 항목입니다.")
	Double longitude
) {}
