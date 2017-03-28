package com.aurea.deadcode.exception;

/**
 * Created by ilshat on 28.03.17.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
