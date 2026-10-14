package com.wo.module.api.notary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wo.module.notary.service.DuplicateNotaryPhoneException;

@ControllerAdvice(basePackages = "com.wo.module.api")
public class NotaryApiExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(NotaryApiExceptionHandler.class);

	@ExceptionHandler(NotaryContactValidationException.class)
	public ResponseEntity<ApiErrorResponse> validation(NotaryContactValidationException exception) {
		return error(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(NotaryContactNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> notFound(NotaryContactNotFoundException exception) {
		return error(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(DuplicateNotaryPhoneException.class)
	public ResponseEntity<ApiErrorResponse> duplicatePhone(DuplicateNotaryPhoneException exception) {
		return error(HttpStatus.CONFLICT, exception.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> unreadable(HttpMessageNotReadableException exception) {
		return error(HttpStatus.BAD_REQUEST, "Request body must be JSON with phoneNo and email");
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ApiErrorResponse> unsupportedType(HttpMediaTypeNotSupportedException exception) {
		return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Content-Type must be application/json");
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiErrorResponse> methodNotAllowed(HttpRequestMethodNotSupportedException exception) {
		return error(HttpStatus.METHOD_NOT_ALLOWED, "Use HTTP PUT");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> unexpected(Exception exception) {
		logger.error("Notary contact update failed", exception);
		return error(HttpStatus.INTERNAL_SERVER_ERROR, "Notary contact could not be updated");
	}

	private ResponseEntity<ApiErrorResponse> error(HttpStatus status, String message) {
		ApiErrorResponse body = new ApiErrorResponse(status.value(), status.getReasonPhrase(), message);
		return ResponseEntity.status(status).body(body);
	}
}
