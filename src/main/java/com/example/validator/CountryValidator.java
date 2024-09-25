package com.example.validator;

import com.example.requset.CountryInsertRequest;
import com.example.requset.CountryModifyRequest;

public class CountryValidator {
    public boolean validate(final CountryInsertRequest countryInsertRequest) {
        return validateName(countryInsertRequest.getName());
    }

    public boolean validate(final CountryModifyRequest countryModifyRequest, final int id) {
        return id >= 0
                && countryModifyRequest.getId() == id
                && validateName(countryModifyRequest.getName());
    }

    private boolean validateName(final String firstCountryName) {
        final String name = firstCountryName;
        return name != null
                && !name.isEmpty();
    }
}
