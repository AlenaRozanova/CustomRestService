package com.example.service;

import com.example.AbstractTest;
import com.example.data.dao.CountryDao;
import com.example.mapper.CountryMapper;
import com.example.requset.CountryInsertRequest;
import com.example.requset.CountryModifyRequest;
import com.example.response.CountryResponse;
import com.example.validator.CountryValidator;
import com.example.data.model.BankEntity;
import com.example.exception.CountryNotFoundException;
import com.example.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest extends AbstractTest {
    @Mock
    private CountryDao countryDao;

    @InjectMocks
    private CountryServiceImpl service;

    @Mock
    private CountryMapper mapper;
    @Spy
    private CountryValidator validator;

    @Test
    void testAddCountry() {
        when(countryDao.insertCountry(any())).thenReturn(true);
        CountryInsertRequest countryInsertRequest =
                new CountryInsertRequest("name");
        assertDoesNotThrow(() -> service.addCountry(countryInsertRequest));
    }

    @Test
    void testAddCountryWithWrongProperties() {
        CountryInsertRequest countryInsertRequest =
                new CountryInsertRequest(null);
        assertThrows(BadRequestException.class, () -> service.addCountry(countryInsertRequest));
    }

    @Test
    void testGetCountryById() {
        when(countryDao.selectCountryById(1)).thenReturn(Optional.ofNullable(countryEntityFirst));
        CountryResponse expected = new CountryResponse(1,
                countryEntityFirst.getName(),
                countryEntityFirst.getBankEntities().stream().map(BankEntity::getName).toList());
        when(mapper.countryEntityToCountryResponse(countryEntityFirst)).thenReturn(expected);
        final CountryResponse actual = service.getCountryById(1);

        assertEquals(expected, actual);
    }

    @Test
    void testGetCountryByNotExistingCountryId() {
        when(countryDao.selectCountryById(4)).thenReturn(Optional.empty());
        assertThrows(CountryNotFoundException.class, () -> service.getCountryById(4));
    }

    @Test
    void testUpdateCountry() {
        CountryModifyRequest countryModifyRequest
                = new CountryModifyRequest(1,
                "nameChanged");
        assertDoesNotThrow(() -> service.updateCountry(countryModifyRequest, 1));
    }

    @Test
    void testUpdateCountryWithWrongPathVariableId() {
        CountryModifyRequest countryModifyRequest
                = new CountryModifyRequest(1,
                "nameChanged");
        assertThrows(BadRequestException.class, () -> service.updateCountry(countryModifyRequest, 2));
    }

    @Test
    void testUpdateCountryWithNotExistingCountryId() {
        CountryModifyRequest countryModifyRequest
                = new CountryModifyRequest(5,
                "nameChanged");
        doThrow(new CountryNotFoundException(4)).when(countryDao).updateCountry(any(), eq(5));
        assertThrows(CountryNotFoundException.class, () -> service.updateCountry(countryModifyRequest, 5));
    }

    @Test
    void testDeleteCountryById() {
        when(countryDao.deleteCountryById(1)).thenReturn(true);
        assertDoesNotThrow(() -> service.deleteCountryById(1));
    }

    @Test
    void testDeleteCountryByWrongId() {
        when(countryDao.deleteCountryById(4)).thenThrow(new CountryNotFoundException(4));
        assertThrows(CountryNotFoundException.class, () -> service.deleteCountryById(4));
    }
}