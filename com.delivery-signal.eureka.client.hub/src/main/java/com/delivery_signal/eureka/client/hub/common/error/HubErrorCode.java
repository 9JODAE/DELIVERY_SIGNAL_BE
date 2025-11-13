package com.delivery_signal.eureka.client.hub.common.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HubErrorCode implements ErrorCode {

	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
	OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
	RESTORE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "재고 복구에 실패했습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getCode() {
		return this.name();
	}
}