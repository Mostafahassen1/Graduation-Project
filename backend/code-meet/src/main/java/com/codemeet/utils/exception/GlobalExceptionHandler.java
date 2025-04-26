package com.codemeet.utils.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(
        EntityNotFoundException e
    ) {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("error", e.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(info);
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateResourceException(
        DuplicateResourceException e
    ) {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("error", e.getMessage());
        info.put("resource", e.getResource().name());
        return ResponseEntity.status(CONFLICT).body(info);
    }

    @ExceptionHandler(IllegalActionException.class)
    public ResponseEntity<Map<String, String>> handleIllegalActionException(
        IllegalActionException e
    ) {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("error", e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(info);
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(
        AuthenticationException e
    ) {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("error", e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(info);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingRequestParameterException(
        MissingServletRequestParameterException e
    ) {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("error", "Parameter is missing");
        info.put("name", e.getParameterName());
        info.put("type", e.getParameterType());
        return ResponseEntity.status(BAD_REQUEST).body(info);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e
    ) {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("error", e.getMessage());
        info.put("requiredType", e.getRequiredType().getSimpleName());
        info.put("passedValue", e.getValue().toString());
        return ResponseEntity.status(BAD_REQUEST).body(info);
    }
}
