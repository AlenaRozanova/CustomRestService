package com.example.exception;

import java.text.MessageFormat;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(final Object id) {
        super(MessageFormat.format("Entity with id {0}", id));
    }

    public EntityNotFoundException(final String message) {
        super(message);
    }
}
