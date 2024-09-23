package com.example.exception;

import java.text.MessageFormat;

public class BankNotFoundException extends EntityNotFoundException {
    public BankNotFoundException(int id) {
        super(MessageFormat.format("Bank with id {0} not found!", id));
    }

    public BankNotFoundException(final String message) {
        super(message);
    }
}
