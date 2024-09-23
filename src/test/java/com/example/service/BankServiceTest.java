package com.example.service;

import com.example.AbstractTest;
import com.example.data.dao.BankDao;
import com.example.mapper.BankMapper;
import com.example.requset.BankInsertRequest;
import com.example.requset.BankModifyRequest;
import com.example.response.BankResponse;
import com.example.validator.BankValidator;
import com.example.exception.CountryNotFoundException;
import com.example.exception.BadRequestException;
import com.example.exception.BankNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankServiceTest extends AbstractTest {
    @Mock
    private BankDao bankDao;

    @InjectMocks
    private BankServiceImpl service;

    @Mock
    private BankMapper mapper;
    @Spy
    private BankValidator validator;

    @Test
    void testAddBank() {
        when(bankDao.insertBank(any())).thenReturn(true);
        BankInsertRequest bankInsertRequest = new BankInsertRequest("name", 1);
        assertDoesNotThrow(() -> service.addBank(bankInsertRequest));
    }

    @Test
    void testAddBankWithWrongProperties() {
        BankInsertRequest bankInsertRequest = new BankInsertRequest("name", -1);
        assertThrows(BadRequestException.class, () -> service.addBank(bankInsertRequest));
    }

    @Test
    void testGetBankById() {
        when(bankDao.selectBankById(1)).thenReturn(Optional.ofNullable(bankEntityFirst));
        BankResponse expected = new BankResponse(1,
                bankEntityFirst.getName(),
                countryEntityFirst.getName(),
                List.of(userEntityFirst.getName(), userEntitySecond.getName()));
        when(mapper.bankEntityToBankResponse(bankEntityFirst)).thenReturn(expected);
        final BankResponse actual = service.getBankById(1);

        assertEquals(expected, actual);
    }

    @Test
    void testGetBankByNotExistingBankId() {
        when(bankDao.selectBankById(4)).thenReturn(Optional.empty());
        assertThrows(BankNotFoundException.class, () -> service.getBankById(4));
    }

    @Test
    void testUpdateBank() {
        BankModifyRequest bankModifyRequest = new BankModifyRequest(1, "another bank name", 2);
        assertDoesNotThrow(() -> service.updateBank(bankModifyRequest, 1));
    }

    @Test
    void testUpdateBankWithWrongPathVariableId() {
        BankModifyRequest bankModifyRequest = new BankModifyRequest(1, "another bank name", 2);
        assertThrows(BadRequestException.class, () -> service.updateBank(bankModifyRequest, 2));
    }

    @Test
    void testUpdateBankWithNotExistingBankId() {
        BankModifyRequest bankModifyRequest = new BankModifyRequest(4, "another bank name", 2);
        doThrow(new BankNotFoundException(4)).when(bankDao).updateBank(any(), eq(4));
        assertThrows(BankNotFoundException.class, () -> service.updateBank(bankModifyRequest, 4));
    }

    @Test
    void testUpdateBankWithNotExistingCountryId() {
        BankModifyRequest bankModifyRequest = new BankModifyRequest(1, "another bank name", 4);
        doThrow(new CountryNotFoundException(4)).when(bankDao).updateBank(any(), eq(1));
        assertThrows(CountryNotFoundException.class, () -> service.updateBank(bankModifyRequest, 1));
    }

    @Test
    void testDeleteBankById() {
        when(bankDao.deleteBankById(1)).thenReturn(true);
        assertDoesNotThrow(() -> service.deleteBankById(1));
    }

    @Test
    void testDeleteBankByWrongId() {
        when(bankDao.deleteBankById(4)).thenThrow(new BankNotFoundException(4));
        assertThrows(BankNotFoundException.class, () -> service.deleteBankById(4));
    }


}