package com.delivery_signal.eureka.client.hub.common.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HubErrorCode implements ErrorCode {

	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getCode() {
		return this.name();
	}
}