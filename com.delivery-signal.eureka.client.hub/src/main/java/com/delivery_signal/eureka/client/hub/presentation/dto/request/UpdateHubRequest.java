package com.delivery_signal.eureka.client.hub.presentation.dto.request;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateHubRequest(
	@Size(min = 5, max = 20, message = "이름은 5자 이상 20자 이하이어야 합니다.")
	@NotBlank(message = "허브 이름은 필수 입력 항목입니다.")
	String name,

	@Size(min = 12, max = 100, message = "주소는 12자 이상 100자 이하이어야 합니다.")
	@NotBlank(message = "주소는 필수 입력 항목입니다.")
	String address,

	@Range(min = 33, max = 39, message = "위도는 33 ~ 39 사이여야 합니다.")
	@NotNull(message = "위도는 필수 입력 항목입니다.")
	Double latitude,

	@Range(min = 124, max = 132, message = "경도는 124 ~ 132 사이여야 합니다.")
	@NotNull(message = "경도는 필수 입력 항목입니다.")
	Double longitude
) {
}