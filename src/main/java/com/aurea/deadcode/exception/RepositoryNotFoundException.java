package com.aurea.deadcode.exception;

/**
 * Created by ilshat on 28.03.17.
 */
public class RepositoryNotFoundException extends RuntimeException {

    private Long id;

    public RepositoryNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
