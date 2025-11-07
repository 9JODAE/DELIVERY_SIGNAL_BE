package com.delivery_signal.eureka.client.gateway.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements ErrorCode {

    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 서명입니다."),
    EXPIRED(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다."),
	MALFORMED(HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰입니다."),
	UNSUPPORTED(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰 형식입니다."),
	INVALID_CLAIMS(HttpStatus.UNAUTHORIZED, "토큰 클레임이 비어있거나 유효하지 않습니다.")
    ;

    private final HttpStatus status;
    private final String message;

	@Override
	public String getCode() {
		return this.name();
	}
}