package com.aurea.deadcode.exception;

/**
 * Created by ilshat on 30.03.17.
 */
public class CantRemoveRepositoryException extends RuntimeException {
    public CantRemoveRepositoryException(String message) {
        super(message);
    }
}
