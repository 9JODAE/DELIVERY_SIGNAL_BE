package com.delivery_signal.eureka.client.gateway.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

	HttpStatus getStatus();

	String getCode();

	String getMessage();

}
