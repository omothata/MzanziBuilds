package com.mzanzibuilds.backend.common;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationError(
      MethodArgumentNotValidException exception,
      HttpServletRequest request
  ) {
    Map<String, String> fieldErrors = new LinkedHashMap<>();

    for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
      fieldErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
    }

    ApiErrorResponse response = new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "Validation failed",
        request.getRequestURI(),
        fieldErrors
    );

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiErrorResponse> handleResponseStatusException(
      ResponseStatusException exception,
      HttpServletRequest request
  ) {
    HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
    ApiErrorResponse response = new ApiErrorResponse(
        status.value(),
        status.getReasonPhrase(),
        exception.getReason() != null ? exception.getReason() : "Request failed",
        request.getRequestURI(),
        Map.of()
    );

    return ResponseEntity.status(status).body(response);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ApiErrorResponse> handleDataAccessException(
      DataAccessException exception,
      HttpServletRequest request
  ) {
    log.error("Database error while handling {}", request.getRequestURI(), exception);

    ApiErrorResponse response = new ApiErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        "Database error. Check that your schema matches the current backend model.",
        request.getRequestURI(),
        Map.of()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
      Exception exception,
      HttpServletRequest request
  ) {
    log.error("Unexpected error while handling {}", request.getRequestURI(), exception);

    ApiErrorResponse response = new ApiErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        "Unexpected server error",
        request.getRequestURI(),
        Map.of()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
