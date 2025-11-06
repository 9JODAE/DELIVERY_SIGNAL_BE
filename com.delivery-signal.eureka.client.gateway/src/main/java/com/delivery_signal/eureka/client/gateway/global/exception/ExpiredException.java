package com.delivery_signal.eureka.client.gateway.global.exception;

import com.delivery_signal.eureka.client.gateway.global.exception.errorcode.ErrorCode;

import lombok.Getter;

@Getter
public class ExpiredException extends RuntimeException {

	private final ErrorCode errorCode;

	public ExpiredException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ExpiredException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}
}
