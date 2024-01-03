package com.example.expandapistest.controllers;

import com.example.expandapistest.exception.DuplicateException;
import com.example.expandapistest.exception.NotFoundException;
import com.example.expandapistest.exception.TableExistsException;
import com.example.expandapistest.models.dto.ApiErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class RestExceptionHandlerController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiErrorResponseDto(NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ApiErrorResponseDto(UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDto> handleRequestNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        e.getBindingResult()
                .getFieldErrors().forEach(error -> errors.add(error.getField() + ":" + error.getDefaultMessage()));
        e.getBindingResult()
                .getGlobalErrors()
                .forEach(error -> errors.add(error.getObjectName() + ":" + error.getDefaultMessage()));
        String message = "Validation of request failing '%s".formatted(String.join(", ", errors));
        return ResponseEntity.status(BAD_REQUEST).body(new ApiErrorResponseDto(BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApiErrorResponseDto> handleDuplicateException(DuplicateException e) {
        return ResponseEntity.status(CONFLICT).body(new ApiErrorResponseDto(CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(TableExistsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleTableExistsException(TableExistsException e){
        return ResponseEntity.status(CONFLICT).body(new ApiErrorResponseDto(CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDto> handleUnknownException(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiErrorResponseDto(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

}
