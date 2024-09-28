package com.example.data.dao;

import com.example.AbstractDaoTest;
import com.example.data.model.BankEntity;
import com.example.data.model.UserEntity;
import com.example.exception.BankNotFoundException;
import com.example.exception.PathVariableException;
import com.example.exception.SQLRuntimeException;
import com.example.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest extends AbstractDaoTest {
    private static UserDao userDao;

    @BeforeAll
    public static void injectObjects() {
        userDao = injector.getInstance(UserDao.class);
    }

    @Test
    void testInsertUserSQLException() {
        assertThrows(SQLRuntimeException.class, () -> userDao.insertUser(new UserEntity()));
    }

    @Test
    void insertUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("man");
        userEntity.setOld(19);
        userEntity.setSex("male");
        userEntity.setEmail("some@mail.ru");
        final boolean b = userDao.insertUser(userEntity);
        assertTrue(b);
        final Optional<UserEntity> createdUserOpt = userDao.selectUserById(4);
        assertTrue(createdUserOpt.isPresent());
        final UserEntity userEntity1 = createdUserOpt.get();
        assertEquals(4, userEntity1.getId());
        assertEquals(userEntity.getName(), userEntity1.getName());
        assertEquals(userEntity.getEmail(), userEntity1.getEmail());
        assertEquals(userEntity.getSex(), userEntity1.getSex());
    }

    @Test
    void selectUsersByBankId() {
        final Set<UserEntity> userEntitySet = userDao.selectUsersByBankId(1);
        for (final UserEntity userEntity : userEntitySet) {
            assertTrue(userEntity.getId() > 0);
        }
        assertEquals(2, userEntitySet.size());
    }

    @Test
    void selectUsersByNotExistingBankId() {
        assertThrows(BankNotFoundException.class, () -> userDao.selectUsersByBankId(4));
    }

    @Test
    void testUpdateUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("name");
        userEntity.setSex("male");
        userEntity.setEmail("some@mail.ru");

        final boolean b = userDao.updateUser(userEntity, userEntity.getId());
        assertTrue(b);
        final UserEntity updatedUser = userDao.selectUserById(1).get();
        assertEquals(updatedUser.getId(), userEntity.getId());
        assertEquals(updatedUser.getName(), userEntity.getName());
        assertEquals(updatedUser.getEmail(), userEntity.getEmail());
        assertEquals(updatedUser.getSex(), userEntity.getSex());
        assertTrue(updatedUser.getBankEntitySet().size() > 0);
    }

    @Test
    void testUpdateUserWithNotExistingId() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(4);
        userEntity.setName("name");
        userEntity.setSex("male");
        userEntity.setEmail("some@mail.ru");

        assertThrows(UserNotFoundException.class, () -> userDao.updateUser(userEntity, userEntity.getId()));
    }

    @Test
    void deleteUserById() {
        final boolean b = userDao.deleteUserById(1);
        assertTrue(b);
    }

    @Test
    void deleteUserByNotExistingId() {
        assertThrows(UserNotFoundException.class, () -> userDao.deleteUserById(4));
    }

    @Test
    void testSelectUserById() {
        final Optional<UserEntity> userEntityOpt = userDao.selectUserById(1);
        assertTrue(userEntityOpt.isPresent());
        final UserEntity userEntity = userEntityOpt.get();
        assertEquals("Вася Пупкин", userEntity.getName());
        assertEquals("male", userEntity.getSex());
        assertEquals(19, userEntity.getOld());
        assertEquals("pupkin@mail.ru", userEntity.getEmail());
        final List<String> usersBanksName = userEntity.getBankEntitySet().stream().map(BankEntity::getName).toList();
        assertEquals(List.of("СБЕР", "Citigroup"), usersBanksName);
    }

    @Test
    void testSelectUsersByNotExistingId() {
        final Optional<UserEntity> userEntityOpt = userDao.selectUserById(4);

        assertTrue(userEntityOpt.isEmpty());
    }

    @Test
    void testIsExistById() {
        final boolean existById = userDao.isExistById(1);

        assertTrue(existById);
    }

    @Test
    void testIfExistByNotExistingId() {
        final boolean existById = userDao.isExistById(4);

        assertFalse(existById);
    }
}