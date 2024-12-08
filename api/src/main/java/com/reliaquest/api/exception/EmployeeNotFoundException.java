package com.reliaquest.api.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String id) {
        super("Employee not found for ID: " + id);
    }
}
