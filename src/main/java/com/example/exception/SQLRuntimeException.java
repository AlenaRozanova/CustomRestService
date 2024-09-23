package com.example.exception;

public class SQLRuntimeException extends RuntimeException {
    public SQLRuntimeException(String message) {
        super(message);
    }
}
