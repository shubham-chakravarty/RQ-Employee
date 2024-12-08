package com.reliaquest.api.controller;

import com.reliaquest.api.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
