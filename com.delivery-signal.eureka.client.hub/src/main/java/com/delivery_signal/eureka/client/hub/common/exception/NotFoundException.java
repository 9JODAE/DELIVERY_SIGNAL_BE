package com.delivery_signal.eureka.client.hub.common.exception;

import com.delivery_signal.eureka.client.hub.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

	private final ErrorCode errorCode;

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public NotFoundException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}


	public NotFoundException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}
}
