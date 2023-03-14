package com.example.tradingplatform.exception;

public class ResNotFoundException extends RuntimeException {
    public ResNotFoundException(String message) {
        super(message);
    }

    public ResNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
