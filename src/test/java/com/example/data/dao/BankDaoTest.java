package com.example.data.dao;

import com.example.AbstractDaoTest;
import com.example.data.model.BankEntity;
import com.example.data.model.CountryEntity;
import com.example.data.model.UserEntity;
import com.example.exception.BankNotFoundException;
import com.example.exception.CountryNotFoundException;
import com.example.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BankDaoTest extends AbstractDaoTest {
    private static BankDao bankDao;

    @BeforeAll
    public static void injectObjects() {
        bankDao = injector.getInstance(BankDao.class);
    }

    @Test
    void testInsertBank() {
        BankEntity bankEntity = new BankEntity();
        CountryEntity country = new CountryEntity();
        country.setId(2);
        bankEntity.setCountry(country);
        bankEntity.setName("bank name");
        final boolean b = bankDao.insertBank(bankEntity);

        assertTrue(b);
        final Optional<BankEntity> newBankEntityOpt = bankDao.selectBankById(4);
        assertTrue(newBankEntityOpt.isPresent());
        final BankEntity newBank = newBankEntityOpt.get();
        assertEquals(4, newBank.getId());
        assertEquals(bankEntity.getName(), newBank.getName());
        assertEquals(bankEntity.getCountry().getId(), country.getId());
    }

    @Test
    void testInsertBankWithWrongCountryId() {
        BankEntity bankEntity = new BankEntity();
        CountryEntity country = new CountryEntity();
        country.setId(3);
        bankEntity.setCountry(country);
        bankEntity.setName("bank name");
        assertThrows(CountryNotFoundException.class, () -> bankDao.insertBank(bankEntity));
    }

    @Test
    void testSelectBankById() {
        final Optional<BankEntity> bankEntityOptional = bankDao.selectBankById(1);
        assertTrue(bankEntityOptional.isPresent());
        final BankEntity bankEntity = bankEntityOptional.get();
        assertEquals(1, bankEntity.getId());
        assertEquals("СБЕР", bankEntity.getName());
        final CountryEntity country = bankEntity.getCountry();
        assertEquals(1, country.getId());
        assertEquals("Россия", country.getName());
        final Set<UserEntity> userEntities = bankEntity.getUserEntities();
        assertEquals(2, userEntities.size());
        for (final UserEntity userEntity : userEntities) {
            assertTrue(userEntity.getId() > 0);
            assertFalse(userEntity.getName().isEmpty());
            assertFalse(userEntity.getEmail().isEmpty());
        }
    }

    @Test
    void testSelectBankByNotExistingId() {
        assertTrue(bankDao.selectBankById(4).isEmpty());
    }

    @Test
    void testSelectBanksByCountryId() {
        final Set<BankEntity> bankEntities = bankDao.selectBanksByCountryId(1);
        for (final BankEntity bankEntity : bankEntities) {
            assertTrue(bankEntity.getId() > 0);
        }
        assertEquals(2, bankEntities.size());
    }

    @Test
    void testSelectBanksByNotExistingCountryId() {
        assertThrows(CountryNotFoundException.class, () -> bankDao.selectBanksByCountryId(3));
    }

    @Test
    void testSelectBanksByUserId() {
        final Set<BankEntity> bankEntities = bankDao.selectBanksByUserId(1);
        for (final BankEntity bankEntity : bankEntities) {
            assertTrue(bankEntity.getId() > 0);
        }
        assertEquals(2, bankEntities.size());
    }

    @Test
    void testSelectBanksByNotExistingUserId() {
        assertThrows(UserNotFoundException.class, () -> bankDao.selectBanksByUserId(4));
    }

    @Test
    void testIsExistById() {
        assertTrue(bankDao.isExistById(1));
    }

    @Test
    void testIsExistByNotExistingId() {
        assertFalse(bankDao.isExistById(4));
    }

    @Test
    void isBanksExistById() {
        assertTrue(bankDao.isBanksExistById(Set.of(1, 2, 3)));
    }

    @Test
    void isBanksExistByNotExistingId() {
        assertFalse(bankDao.isBanksExistById(Set.of(1, 2, 4)));
    }

    @Test
    void testUpdateBank() {
        BankEntity bankEntity = new BankEntity();
        CountryEntity country = new CountryEntity();
        country.setId(2);
        bankEntity.setCountry(country);
        bankEntity.setId(1);
        bankEntity.setName("bank name");
        final boolean b = bankDao.updateBank(bankEntity, bankEntity.getId());
        assertTrue(b);

        final Optional<BankEntity> updatedBankOpt = bankDao.selectBankById(1);
        assertTrue(updatedBankOpt.isPresent());
        final BankEntity updatedBank = updatedBankOpt.get();
        assertEquals(updatedBank.getId(), bankEntity.getId());
        assertEquals(updatedBank.getName(), bankEntity.getName());
        assertEquals(updatedBank.getCountry().getId(), country.getId());
        assertEquals(2, updatedBank.getUserEntities().size());
    }

    @Test
    void testUpdateBankWithNotExistingCountryId() {
        BankEntity bankEntity = new BankEntity();
        CountryEntity country = new CountryEntity();
        country.setId(3);
        bankEntity.setCountry(country);
        bankEntity.setId(1);
        bankEntity.setName("new name");
        assertThrows(CountryNotFoundException.class, () -> bankDao.updateBank(bankEntity, bankEntity.getId()));
    }

    @Test
    void testDeleteBankById() {
        assertTrue(bankDao.selectBankById(1).isPresent());
        bankDao.deleteBankById(1);
        assertFalse(bankDao.selectBankById(1).isPresent());
    }

    @Test
    void testDeleteBankByNotExistingId() {
        assertThrows(BankNotFoundException.class, () -> bankDao.deleteBankById(4));
    }

    @Test
    void testDeleteBanksByCountryId() {
        final Set<BankEntity> bankEntities = bankDao.selectBanksByCountryId(1);
        bankDao.deleteBanksByCountryId(1);
        final List<Integer> banks = bankEntities.stream().map(BankEntity::getId).toList();
        for (final Integer bank : banks) {
            assertFalse(bankDao.isExistById(bank));
        }
    }

    @Test
    void testDeleteBanksByNotExistingCountryId() {
        assertThrows(CountryNotFoundException.class, () -> bankDao.deleteBanksByCountryId(3));
    }
}