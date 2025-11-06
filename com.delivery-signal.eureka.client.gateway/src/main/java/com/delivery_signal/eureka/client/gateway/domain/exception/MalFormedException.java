package com.delivery_signal.eureka.client.gateway.domain.exception;

import com.delivery_signal.eureka.client.gateway.domain.exception.errorcode.ErrorCode;

import lombok.Getter;

@Getter
public class MalFormedException extends RuntimeException {

	private final ErrorCode errorCode;

	public MalFormedException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public MalFormedException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}
}
