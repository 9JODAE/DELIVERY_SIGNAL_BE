package com.delivery_signal.eureka.client.hub.presentation.request;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateHubRequest {

	@NotBlank(message = "이름은 필수 입력 항목입니다.")
	private String name;

	@NotBlank(message = "주소는 필수 입력 항목입니다.")
	private String address;

	@Range(min = 33, max = 39, message = "위도는 33 ~ 39 사이여야 합니다.")
	private double latitude;

	@Range(min = 124, max = 132, message = "경도는 124 ~ 132 사이여야 합니다.")
	private double longitude;
}
