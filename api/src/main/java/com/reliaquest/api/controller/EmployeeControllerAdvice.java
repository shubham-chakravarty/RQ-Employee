package com.reliaquest.api.controller;

import com.reliaquest.api.exception.EmployeeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
     * Handles validation exceptions triggered by {@code @Valid} annotation during request body validation.
     * <p>
     * This method is invoked whenever a {@link MethodArgumentNotValidException} is thrown in the application,
     * typically due to validation errors in the incoming request body.
     * It formats the validation errors into a user-friendly response structure and returns an HTTP status of 400 (Bad Request).
     * </p>
     *
     * @param ex the exception that encapsulates the validation errors
     * @return a {@link ResponseEntity} containing the error message and a list of detailed validation error descriptions.
     *         Each error description specifies the invalid field and the corresponding error message.
     *
     * @see MethodArgumentNotValidException
     * @see ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Validation failed");
        errorResponse.put("details", ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> String.format("Field '%s' %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList()));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
