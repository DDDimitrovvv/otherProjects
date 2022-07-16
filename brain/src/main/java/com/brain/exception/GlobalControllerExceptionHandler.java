package com.brain.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {

        return new ResponseEntity<>(
                ex.getConstraintViolations().stream().map(ConstraintViolation::getMessageTemplate),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }
}
