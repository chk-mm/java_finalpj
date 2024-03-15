package com.harbourspace.tracker.config;

import com.harbourspace.tracker.error.ErrorResponse;
import com.harbourspace.tracker.error.NotFoundException;
import com.harbourspace.tracker.error.AuthorizationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
        return handleResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        return handleResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { AuthorizationException.class })
    protected ResponseEntity<Object> unauthorizedError(RuntimeException ex, WebRequest request) {
        return handleResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = { IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        return handleResponse(ex, HttpStatus.CONFLICT, request);

    }

    @ExceptionHandler(value = { AuthenticationException.class })
    protected ResponseEntity<Object> handleUnauthorized(RuntimeException ex, WebRequest request) {
        return handleResponse(ex, HttpStatus.UNAUTHORIZED, request);
    }

    private ResponseEntity<Object> handleResponse(Exception ex, HttpStatus status, WebRequest request) {
        var response = new ErrorResponse(ex.getMessage(), status.value());
        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }
}
