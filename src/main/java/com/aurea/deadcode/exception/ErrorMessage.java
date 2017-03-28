package com.aurea.deadcode.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by ilshat on 28.03.17.
 */
public class ErrorMessage {
    private int status;
    private String error;
    private String message;

    ErrorMessage(HttpStatus status, String message) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
