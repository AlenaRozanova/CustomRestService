package com.example.data.dao;

import com.example.AbstractDaoTest;
import com.example.data.model.BankEntity;
import com.example.data.model.UserEntity;
import com.example.exception.BadRequestException;
import com.example.exception.BankNotFoundException;
import com.example.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserBankDaoTest extends AbstractDaoTest {
    static Stream<Arguments> inputData() {
        return Stream.of(
                Arguments.of(List.of(1, 2), false),
                Arguments.of(List.of(1, 3), false),
                Arguments.of(List.of(1, 2, 3), false),
                Arguments.of(List.of(2, 3), false),
                Arguments.of(List.of(2), true)
        );
    }

    private static UserBankDao userBankDao;
    private static BankDao bankDao;

    private static UserDao userDao;


    @BeforeAll
    static void injectObjects() {
        userBankDao = injector.getInstance(UserBankDao.class);
        bankDao = injector.getInstance(BankDao.class);
        userDao = injector.getInstance(UserDao.class);
    }

    @Test
    void testInsertUserBanksByUserId() {
        final boolean b = userBankDao.insertUserBanksByUserId(List.of(2), 1);
        assertTrue(b);
        final Optional<UserEntity> userEntity = userDao.selectUserById(1);
        assertTrue(userEntity.isPresent());
        final UserEntity userEntity1 = userEntity.get();
        assertEquals(3, userEntity1.getBankEntitySet().size());
    }

    @Test
    void testInsertUserBanksByNotExistingUserId() {
        assertThrows(UserNotFoundException.class, () -> userBankDao.insertUserBanksByUserId(List.of(2), 4));
    }

    @Test
    void testInsertUserBanksByNotExistingBankId() {
        assertThrows(BankNotFoundException.class, () -> userBankDao.insertUserBanksByUserId(List.of(55), 1));
    }

    @Test
    void testInsertUserBanksByUserIdWithAlreadyExistingBankId() {
        assertThrows(BadRequestException.class, () -> userBankDao.insertUserBanksByUserId(List.of(1, 2), 1));
    }

    @Test
    void testDeleteUserBanksByUserId() {
        final boolean b = userBankDao.deleteUserBanksByUserId(1);
        assertTrue(b);
        final Optional<UserEntity> userEntityOptional = userDao.selectUserById(1);
        assertTrue(userEntityOptional.isPresent());
        final UserEntity userEntity = userEntityOptional.get();
        assertTrue(userEntity.getBankEntitySet().isEmpty());
    }

    @Test
    void testDeleteUserBanksByNotExistingUserId() {
        assertThrows(UserNotFoundException.class, () -> userBankDao.deleteUserBanksByUserId(4));
    }

    @Test
    void testDeleteSpecifiedUserBanksByUserId() {
        final boolean b = userBankDao.deleteUserBanksByUserId(List.of(1), 1);
        assertTrue(b);
        final Optional<UserEntity> userEntityOptional = userDao.selectUserById(1);
        assertTrue(userEntityOptional.isPresent());
        final UserEntity userEntity = userEntityOptional.get();
        assertEquals(1, userEntity.getBankEntitySet().size());
    }

    @Test
    void testDeleteSpecifiedUserBanksByNotExistingUserId() {
        assertThrows(UserNotFoundException.class, () -> userBankDao.deleteUserBanksByUserId(List.of(1), 4));
    }

    @Test
    void testDeleteSpecifiedUserBanksByUserIdWithNotOwnedByUserBankId() {
        assertThrows(BadRequestException.class, () -> userBankDao.deleteUserBanksByUserId(List.of(1, 2), 1));
    }

    @Test
    void testDeleteUserBanksByBankId() {
        final boolean b = userBankDao.deleteUserBanksByBankId(1);
        assertTrue(b);
        final Optional<BankEntity> bankEntityOptional = bankDao.selectBankById(1);
        assertTrue(bankEntityOptional.isPresent());
        final BankEntity bankEntity = bankEntityOptional.get();
        assertTrue(bankEntity.getUserEntities().isEmpty());
    }

    @Test
    void testDeleteUserBanksByNotExistingBankId() {
        assertThrows(BankNotFoundException.class, () -> userBankDao.deleteUserBanksByBankId(4));
    }
}