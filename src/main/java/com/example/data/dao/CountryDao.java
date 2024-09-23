package com.example.data.dao;

import com.example.data.model.CountryEntity;

import java.util.Optional;

public interface CountryDao {
    boolean insertCountry(final CountryEntity country);

    Optional<CountryEntity> selectCountryById(final int id);

    boolean updateCountry(final CountryEntity country, final int id);

    boolean deleteCountryById(final int id);

    boolean isExistById(final int id);
}
