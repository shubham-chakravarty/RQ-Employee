package com.reliaquest.api.controller;

import com.reliaquest.api.exception.EmployeeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class EmployeeControllerAdvice {

    /**
     * Handles {@link EmployeeNotFoundException} by returning a 404 Not Found response.
     *
     * @param ex the thrown exception
     * @return a {@link ResponseEntity} with HTTP 404 and error message.
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        log.error("Error handling Employee Not Found.", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles {@link Throwable} by returning a 500 Internal Server Error response.
     *
     * @param ex the thrown exception
     * @return a {@link ResponseEntity} with HTTP 500 and error message.
     */
    @ExceptionHandler
    protected ResponseEntity<?> handleException(Throwable ex) {
        log.error("Error handling web request.", ex);
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
