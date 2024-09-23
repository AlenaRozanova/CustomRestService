package com.example.data.dao;

import com.example.AbstractDaoTest;
import com.example.data.model.CountryEntity;
import com.example.exception.CountryNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CountryDaoTest extends AbstractDaoTest {
    private static CountryDao countryDao;

    @BeforeAll
    static void inject() {
        countryDao = injector.getInstance(CountryDao.class);
    }

    @Test
    void testInsertCountry() {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setName("fn");

        countryDao.insertCountry(countryEntity);

        final Optional<CountryEntity> newCountryOpt = countryDao.selectCountryById(3);
        assertTrue(newCountryOpt.isPresent());
        final CountryEntity newCountry = newCountryOpt.get();
        assertEquals(3, newCountry.getId());
        assertEquals(countryEntity.getName(), newCountry.getName());
    }

    @Test
    void testSelectCountryById() {
        final Optional<CountryEntity> countryOpt = countryDao.selectCountryById(1);
        assertTrue(countryOpt.isPresent());
        final CountryEntity country = countryOpt.get();
        assertEquals(1, country.getId());
        assertEquals("Россия", country.getName());
    }

    @Test
    void testSelectCountryByNotExistingId() {
        assertThrows(CountryNotFoundException.class, () -> countryDao.selectCountryById(3));
    }

    @Test
    void testUpdateCountry() {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(1);
        countryEntity.setName("fn");

        final boolean b = countryDao.updateCountry(countryEntity, countryEntity.getId());

        assertTrue(b);
        final Optional<CountryEntity> updatedCountryOpt = countryDao.selectCountryById(1);
        assertTrue(updatedCountryOpt.isPresent());
        final CountryEntity updatedCountry = updatedCountryOpt.get();
        assertEquals(updatedCountry.getId(), countryEntity.getId());
        assertEquals(updatedCountry.getName(), countryEntity.getName());
        assertEquals(2, updatedCountry.getBankEntities().size());
    }

    @Test
    void testUpdateCountryWithNotExistingId() {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(3);
        countryEntity.setName("fn");

        assertThrows(CountryNotFoundException.class, () -> countryDao.updateCountry(countryEntity, countryEntity.getId()));
    }

    @Test
    void testDeleteCountryById() {
        final boolean b = countryDao.deleteCountryById(1);
        assertTrue(b);
        assertFalse(countryDao.isExistById(1));
    }

    @Test
    void testDeleteCountryByNotExistingId() {
        assertThrows(CountryNotFoundException.class, () -> countryDao.deleteCountryById(3));
    }

    @Test
    void testIsExistById() {
        assertTrue(countryDao.isExistById(1));
    }

    @Test
    void testIsExistByNotExistingId() {
        assertFalse(countryDao.isExistById(3));
    }
}