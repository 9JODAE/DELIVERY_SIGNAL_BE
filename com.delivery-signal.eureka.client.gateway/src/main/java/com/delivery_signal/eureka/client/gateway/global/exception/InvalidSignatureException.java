package com.delivery_signal.eureka.client.gateway.global.exception;

import com.delivery_signal.eureka.client.gateway.global.exception.errorcode.ErrorCode;

import lombok.Getter;

@Getter
public class InvalidSignatureException extends RuntimeException {

	private final ErrorCode errorCode;

	public InvalidSignatureException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public InvalidSignatureException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}
}
