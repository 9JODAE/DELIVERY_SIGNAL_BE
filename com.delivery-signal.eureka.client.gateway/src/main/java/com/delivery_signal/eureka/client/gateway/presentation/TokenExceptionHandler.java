package com.delivery_signal.eureka.client.gateway.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.delivery_signal.eureka.client.gateway.domain.exception.EmptyClaimsException;
import com.delivery_signal.eureka.client.gateway.domain.exception.ExpiredException;
import com.delivery_signal.eureka.client.gateway.domain.exception.InvalidSignatureException;
import com.delivery_signal.eureka.client.gateway.domain.exception.MalFormedException;
import com.delivery_signal.eureka.client.gateway.domain.exception.UnsupportedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class TokenExceptionHandler {

	@ExceptionHandler(EmptyClaimsException.class)
	public ResponseEntity<ErrorResponse> handleEmptyClaimsException(EmptyClaimsException e) {
		log.warn("토큰 클레임 오류 - 메시지: {}", e.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(
			e.getErrorCode().getCode(),
			e.getErrorCode().getMessage(),
			e.getErrorCode().getStatus()
		);

		return ResponseEntity.status(errorResponse.status()).body(errorResponse);
	}

	@ExceptionHandler(ExpiredException.class)
	public ResponseEntity<ErrorResponse> handleExpiredException(ExpiredException e) {
		log.warn("토큰 만료 - 메시지: {}", e.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(
			e.getErrorCode().getCode(),
			e.getErrorCode().getMessage(),
			e.getErrorCode().getStatus()
		);

		return ResponseEntity.status(errorResponse.status()).body(errorResponse);
	}

	@ExceptionHandler(InvalidSignatureException.class)
	public ResponseEntity<ErrorResponse> handleInvalidSignatureException(InvalidSignatureException e) {
		log.warn("토큰 서명 오류 - 메시지: {}", e.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(
			e.getErrorCode().getCode(),
			e.getErrorCode().getMessage(),
			e.getErrorCode().getStatus()
		);
		return ResponseEntity.status(errorResponse.status()).body(errorResponse);
	}

	@ExceptionHandler(MalFormedException.class)
	public ResponseEntity<ErrorResponse> handleMalFormedException(MalFormedException e) {
		log.warn("토큰 형식 오류 - 메시지: {}", e.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(
			e.getErrorCode().getCode(),
			e.getErrorCode().getMessage(),
			e.getErrorCode().getStatus()
		);
		return ResponseEntity.status(errorResponse.status()).body(errorResponse);
	}

	@ExceptionHandler(UnsupportedException.class)
	public ResponseEntity<ErrorResponse> handleUnsupportedException(UnsupportedException e) {
		log.warn("지원하지 않는 토큰 - 메시지: {}", e.getMessage());

		ErrorResponse errorResponse = new ErrorResponse(
			e.getErrorCode().getCode(),
			e.getErrorCode().getMessage(),
			e.getErrorCode().getStatus()
		);
		return ResponseEntity.status(errorResponse.status()).body(errorResponse);
	}


	/**
	 * 에러 응답 DTO
	 */
	public record ErrorResponse(
		String code,
		String message,
		HttpStatus status
	) {}
}
