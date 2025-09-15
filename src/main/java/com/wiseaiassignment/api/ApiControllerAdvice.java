package com.wiseaiassignment.api;

import com.wiseaiassignment.domain.common.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {
	@ExceptionHandler
	public ResponseEntity<ApiErrorResponse> handleDomainException(DomainException e) {
		log.warn(e.getMessage(), e);
		return ResponseEntity
				.status(e.getType().getStatus())
				.body(ApiErrorResponse.of(e.getMessage()));
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
	public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception e) {
		log.warn(e.getMessage(), e);
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ApiErrorResponse.of("요청 파라미터가 잘못되었습니다."));
	}

	@ExceptionHandler
	public ResponseEntity<ApiErrorResponse> handleBadRequest(MissingServletRequestParameterException e) {
		log.warn(e.getMessage(), e);
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ApiErrorResponse.of("필수 요청 파라미터가 누락되었습니다."));
	}

	@ExceptionHandler
	public ResponseEntity<ApiErrorResponse> handleBadRequest(HttpMessageNotReadableException e) {
		log.warn(e.getMessage(), e);
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ApiErrorResponse.of("요청 본문을 처리하는 중 오류가 발생했습니다. JSON 형식을 확인해주세요."));
	}

}
