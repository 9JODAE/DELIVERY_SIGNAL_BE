package com.delivery_signal.eureka.client.hub.common.exception;

import com.delivery_signal.eureka.client.hub.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class OutOfStockException extends RuntimeException {

	private final ErrorCode errorCode;

	public OutOfStockException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public OutOfStockException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}
}
