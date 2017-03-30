package com.aurea.deadcode.service.integration;

/**
 * Created by ilshat on 30.03.17.
 */
class UnderstandIntegrationException extends RuntimeException {
    UnderstandIntegrationException(String message) {
        super(message);
    }

    UnderstandIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
