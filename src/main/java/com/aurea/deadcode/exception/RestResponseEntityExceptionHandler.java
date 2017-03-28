package com.aurea.deadcode.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by ilshat on 28.03.17.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RepositoryNotFoundException.class})
    public final ResponseEntity<Object> handleException(RepositoryNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String textMessage = "Could not found repository with id " + ex.getId();
        ErrorMessage errorMessage = new ErrorMessage(status, textMessage);
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({RepositoryAlreadyExistsException.class})
    public final ResponseEntity<Object> handleException(RepositoryAlreadyExistsException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        String textMessage = String.format("Repository with URL %s already exists", ex.getUrl());
        ErrorMessage errorMessage = new ErrorMessage(status, textMessage);
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), status, request);
    }
}
