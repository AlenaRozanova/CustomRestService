package com.example.service;

import com.example.exception.BadRequestException;
import com.example.exception.BankNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.data.dao.UserBankDao;
import com.example.requset.UserBankRequest;
import com.example.validator.UserBankValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserBankServiceTest {
    @Mock
    private UserBankDao userBankDao;

    @InjectMocks
    private UserBankServiceImpl service;

    @Spy
    private UserBankValidator validator;

    @Test
    void testAddBanksToUser() {
        when(userBankDao.insertUserBanksByUserId(any(), anyInt())).thenReturn(true);
        UserBankRequest userBankRequest = new UserBankRequest(1, List.of(2, 3));
        assertDoesNotThrow(() -> service.addBanksToUser(userBankRequest));
    }

    @Test
    void testAddBanksToUserWithWrongUserId() {
        when(userBankDao.insertUserBanksByUserId(any(), eq(5))).thenThrow(new UserNotFoundException(5));
        UserBankRequest userBankRequest = new UserBankRequest(5, List.of(2, 3));
        assertThrows(UserNotFoundException.class, () -> service.addBanksToUser(userBankRequest));
    }

    @Test
    void testAddBanksToUserWithWrongBankId() {
        when(userBankDao.insertUserBanksByUserId(any(), anyInt())).thenThrow(new BankNotFoundException(5));
        UserBankRequest userBankRequest = new UserBankRequest(1, List.of(2, 5));
        assertThrows(BankNotFoundException.class, () -> service.addBanksToUser(userBankRequest));
    }

    @Test
    void testAddBanksToUserWithNegativeId() {
        UserBankRequest userBankRequest = new UserBankRequest(-1, List.of(2, 3));
        assertThrows(BadRequestException.class, () -> service.addBanksToUser(userBankRequest));
    }

    @Test
    void testAddBanksToUserWithDuplicatingBankId() {
        UserBankRequest userBankRequest = new UserBankRequest(1, List.of(2, 3, 2));
        assertThrows(BadRequestException.class, () -> service.addBanksToUser(userBankRequest));
    }

    @Test
    void testDeleteBanksFromUser() {
        when(userBankDao.deleteUserBanksByUserId(any(), anyInt())).thenReturn(true);
        UserBankRequest userBankRequest = new UserBankRequest(1, List.of(2, 3));
        assertDoesNotThrow(() -> service.deleteBankFromUser(userBankRequest));
    }

    @Test
    void testDeleteBanksFromUserWithWrongUserId() {
        when(userBankDao.deleteUserBanksByUserId(any(), eq(5))).thenThrow(new UserNotFoundException(5));
        UserBankRequest userBankRequest = new UserBankRequest(5, List.of(2, 3));
        assertThrows(UserNotFoundException.class, () -> service.deleteBankFromUser(userBankRequest));
    }

    @Test
    void testDeleteBanksFromUserWithWrongBankId() {
        when(userBankDao.deleteUserBanksByUserId(any(), anyInt())).thenThrow(new BankNotFoundException(5));
        UserBankRequest userBankRequest = new UserBankRequest(1, List.of(2, 5));
        assertThrows(BankNotFoundException.class, () -> service.deleteBankFromUser(userBankRequest));
    }

    @Test
    void testDeleteBanksFromUserWithNegativeId() {
        UserBankRequest userBankRequest = new UserBankRequest(-1, List.of(2, 3));
        assertThrows(BadRequestException.class, () -> service.deleteBankFromUser(userBankRequest));
    }

    @Test
    void testDeleteBanksFromUserWithDuplicateBankId() {
        UserBankRequest userBankRequest = new UserBankRequest(1, List.of(2, 3, 2));
        assertThrows(BadRequestException.class, () -> service.deleteBankFromUser(userBankRequest));
    }
}