package com.example.exception;

public class NotProvidedException extends RuntimeException {

    public NotProvidedException(final String message) {
        super(message);
    }
}
