package com.aurea.deadcode.exception;

/**
 * Created by ilshat on 28.03.17.
 */
public class RepositoryAlreadyExistsException extends RuntimeException {

    private String url;

    public RepositoryAlreadyExistsException(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
