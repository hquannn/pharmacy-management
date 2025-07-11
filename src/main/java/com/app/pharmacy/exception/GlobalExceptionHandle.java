package com.app.pharmacy.exception;

import com.app.pharmacy.domain.common.ApiResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(Exception ex) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse
                        .builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoResourceFoundException(NoResourceFoundException ex) {
        ErrorCode errorCode = ErrorCode.NO_RESOURCE_FOUND;
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse
                        .builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UN_AUTHORIZE;
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse
                        .builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlingValidationException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        return ResponseEntity
                .badRequest()
                .body(ApiResponse
                        .builder()
                        .code(errorCode.getCode())
                        .message(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage())
                        .build());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse<?>> handlingValidationException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        String errorMessage = "Invalid input: Please ensure all fields are valid.";
        Throwable cause = ex.getCause();
        if (cause instanceof JsonMappingException jsonMappingException) {
            Optional<String> fieldName = jsonMappingException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .findFirst();

            if (fieldName.isPresent()) {
                errorMessage = "Invalid input in field: '" + fieldName.get() + "'. Please ensure it is a valid value.";
            }
        }
        return ResponseEntity
                .badRequest()
                .body(ApiResponse
                        .builder()
                        .code(errorCode.getCode())
                        .message(errorMessage)
                        .build());
    }

    @ExceptionHandler(value = CustomResponseException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomResponseException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(ex.getErrorCode().getStatusCode())
                .body(ApiResponse
                        .builder()
                        .code(ex.getErrorCode().getCode())
                        .message(ex.getErrorCode().getMessage())
                        .build());
    }
}
