package com.example.exception;

import java.text.MessageFormat;

public class CountryNotFoundException extends EntityNotFoundException {
    public CountryNotFoundException(int id) {
        super(MessageFormat.format("Country with id {0} not found!", id));
    }
}
