package com.delivery_signal.eureka.client.hub.common.exception;

import com.delivery_signal.eureka.client.hub.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class RestoreFailedException extends RuntimeException {
	private final ErrorCode errorCode;

	public RestoreFailedException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public RestoreFailedException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}
}
