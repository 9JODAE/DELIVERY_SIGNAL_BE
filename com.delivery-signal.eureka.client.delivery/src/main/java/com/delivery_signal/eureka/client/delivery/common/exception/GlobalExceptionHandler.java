package com.delivery_signal.eureka.client.delivery.common.exception;


import com.delivery_signal.eureka.client.delivery.presentation.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 규칙 위반 예외 처리
     * IllegalStateException, IllegalArgumentException 등
     */
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(Exception e) {
        log.warn("비즈니스 규칙 위반: {}", e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            "BUSINESS_RULE_VIOLATION",
            e.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(ApiResponse.error(errorResponse));
    }

    /**
     * 리소스 없음 예외 처리
     */
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleNotFoundException(java.util.NoSuchElementException e) {
        log.warn("리소스 없음: {}", e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            "RESOURCE_NOT_FOUND",
            e.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(errorResponse));
    }

    /**
     * 입력 검증 실패 예외 처리 (Spring Validation)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("입력 검증 실패: {}", e.getMessage());

        String message = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("입력 검증 실패");

        ErrorResponse errorResponse = new ErrorResponse(
            "VALIDATION_FAILED",
            message,
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(ApiResponse.error(errorResponse));
    }

    /**
     * 바인딩 예외 처리 (Spring Validation)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBindException(BindException e) {
        log.warn("바인딩 예외: {}", e.getMessage());

        String message = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("바인딩 예외");

        ErrorResponse errorResponse = new ErrorResponse(
            "BINDING_ERROR",
            message,
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(ApiResponse.error(errorResponse));
    }

    /**
     * 제약 위반 예외 처리 (PathVariable Validation 등)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleConstraintViolationException(
        ConstraintViolationException e) {
        log.warn("제약 위반: {}", e.getMessage());

        String message = e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .findFirst()
            .orElse("제약 위반이 발생했습니다");

        ErrorResponse errorResponse = new ErrorResponse(
            "CONSTRAINT_VIOLATION",
            message,
            HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.badRequest().body(ApiResponse.error(errorResponse));
    }

    /**
     * 서버 내부 오류 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleInternalServerError(Exception e) {
        log.error("서버 내부 오류 발생", e);

        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "서버 오류가 발생했습니다",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(errorResponse));
    }

    /**
     * 인가되지 않은 접근 예외 처리
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleUnauthorizedException(UnauthorizedException e) {
        log.error("인가되지 않은 접근 시도: {}", e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            e.getErrorCode().getCode(),
            e.getErrorCode().getMessage(),
            HttpStatus.UNAUTHORIZED.value()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(errorResponse));
    }

    /**
     * 에러 응답 DTO
     */
    public record ErrorResponse(
        String code,
        String message,
        Integer status
    ) {
    }
}
