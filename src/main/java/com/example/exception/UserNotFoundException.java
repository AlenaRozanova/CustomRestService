package com.example.exception;

import java.text.MessageFormat;

/**
 * Custom exception class for handling cases when a user does not exist.
 */
public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(int id) {
        super(MessageFormat.format("User with id {0} not found!", id));
    }
}
