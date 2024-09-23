package com.example.service;

import com.example.requset.CountryInsertRequest;
import com.example.requset.CountryModifyRequest;
import com.example.response.CountryResponse;

public interface CountryService {
    void addCountry(final CountryInsertRequest countryInsertRequest);

    CountryResponse getCountryById(final int id);

    void updateCountry(final CountryModifyRequest countryModifyRequest, final int id);

    void deleteCountryById(final int id);
}
