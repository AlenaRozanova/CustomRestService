package com.example.service;

import com.example.data.dao.CountryDao;
import com.example.data.model.CountryEntity;
import com.example.exception.BadRequestException;
import com.example.exception.CountryNotFoundException;
import com.example.mapper.CountryMapper;
import com.example.requset.CountryInsertRequest;
import com.example.requset.CountryModifyRequest;
import com.example.response.CountryResponse;
import com.example.validator.CountryValidator;
import com.google.inject.Inject;

/**
 * Provides methods to interact with the User data.
 */
public class CountryServiceImpl implements CountryService {

    private final CountryDao countryDao;

    private final CountryMapper countryMapper;

    private final CountryValidator countryValidator;

    /**
     * Constructor for the {@link CountryServiceImpl} class.
     *
     * @param countryDao       The {@link CountryDao} instance used for database operations.
     * @param countryMapper    The {@link CountryMapper} instance used for mapping between domain and data models.
     * @param countryValidator The {@link CountryValidator} instance used for validating input data.
     */
    @Inject
    public CountryServiceImpl(final CountryDao countryDao, final CountryMapper countryMapper,
                             final CountryValidator countryValidator) {
        this.countryDao = countryDao;
        this.countryMapper = countryMapper;
        this.countryValidator = countryValidator;
    }

    /**
     * Adds a new country to the database.
     *
     * @param countryInsertRequest The {@link CountryInsertRequest} containing the details of the new country.
     * @throws BadRequestException If the country insert request is invalid.
     */
    @Override
    public void addCountry(final CountryInsertRequest countryInsertRequest) {
        if (!countryValidator.validate(countryInsertRequest))
            throw new BadRequestException("Invalid country insert request");
        countryDao.insertCountry(countryMapper.countryInsertRequestToCountryEntity(countryInsertRequest));
    }

    /**
     * Retrieves an country by its ID from the database.
     *
     * @param id The ID of the country to retrieve.
     * @return The {@link CountryResponse} containing the details of the country, or null if the country was not found.
     * @throws CountryNotFoundException If the country with the given ID was not found in the database.
     */
    @Override
    public CountryResponse getCountryById(final int id) {
        final CountryEntity countryEntity = countryDao.selectCountryById(id).orElseThrow(() -> new CountryNotFoundException(id));
        return countryMapper.countryEntityToCountryResponse(countryEntity);
    }

    /**
     * Updates an existing country in the database.
     *
     * @param countryModifyRequest The {@link CountryModifyRequest} containing the details of the country to update.
     * @param id                  The ID of the country to update.
     * @throws BadRequestException If the country modify request is invalid.
     */
    @Override
    public void updateCountry(final CountryModifyRequest countryModifyRequest, final int id) {
        if (!countryValidator.validate(countryModifyRequest, id))
            throw new BadRequestException("Invalid country modify request");
        countryDao.updateCountry(countryMapper.countryModifyRequestToCountryEntity(countryModifyRequest), id);
    }

    /**
     * Deletes an country from the database by its ID.
     *
     * @param id The ID of the country to delete.
     */
    @Override
    public void deleteCountryById(final int id) {
        countryDao.deleteCountryById(id);
    }
}
