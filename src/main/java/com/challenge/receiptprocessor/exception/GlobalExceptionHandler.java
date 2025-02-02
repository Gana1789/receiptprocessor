package com.challenge.receiptprocessor.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Error processing the input: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body("Invalid Receipt, please verify the input");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        log.error("Constraint violation: {}", constraintViolationException.getMessage(), constraintViolationException);
        return ResponseEntity.badRequest().body("Invalid Receipt, please verify the input");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException illegalStateException) {
        log.error("Illegal state: {}", illegalStateException.getMessage(), illegalStateException);
        return ResponseEntity.badRequest().body("Invalid Receipt, please verify the input");
    }
}
