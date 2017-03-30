package com.aurea.deadcode.service.integration;

/**
 * Created by ilshat on 30.03.17.
 */
class GitHubIntegrationException extends RuntimeException {
    GitHubIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
