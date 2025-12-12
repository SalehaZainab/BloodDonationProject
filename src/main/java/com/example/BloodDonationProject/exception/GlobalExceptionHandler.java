package com.example.BloodDonationProject.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.BloodDonationProject.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<ApiResponse<Void>> handleAlreadyExists(AlreadyExistsException ex) {
		return build(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
		return build(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String field = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(field, message);
		});
		ApiResponse<Map<String, String>> body = ApiResponse.failure("Validation failed", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
	}

	private ResponseEntity<ApiResponse<Void>> build(HttpStatus status, String message) {
		ApiResponse<Void> body = ApiResponse.failure(message);
		return ResponseEntity.status(status).body(body);
	}
}
