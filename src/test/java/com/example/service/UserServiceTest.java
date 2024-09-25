package com.example.service;

import com.example.AbstractTest;
import com.example.data.dao.UserDao;
import com.example.exception.BadRequestException;
import com.example.exception.UserNotFoundException;
import com.example.mapper.UserMapper;
import com.example.requset.UserInsertRequest;
import com.example.requset.UserModifyRequest;
import com.example.response.UserResponse;
import com.example.validator.UserValidator;
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
class UserServiceTest extends AbstractTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper mapper;
    @Spy
    private UserValidator validator;


    @Test
    void testAddUser() {
        when(userDao.insertUser(any())).thenReturn(true);
        UserInsertRequest userInsertRequest = new UserInsertRequest("name", "email", "sex");
        assertDoesNotThrow(() -> userService.addUser(userInsertRequest));
    }

    @Test
    void testAddUserWithWrongEmail() {
        UserInsertRequest userInsertRequest = new UserInsertRequest("name", null, "sex");
        assertThrows(BadRequestException.class, () -> userService.addUser(userInsertRequest));
    }

    @Test
    void testAddUserWithWrongName() {
        UserInsertRequest userInsertRequest = new UserInsertRequest(null, "email", "sex");
        assertThrows(BadRequestException.class, () -> userService.addUser(userInsertRequest));
    }

    @Test
    void testGetUserById() {
        when(userDao.selectUserById(3)).thenReturn(Optional.ofNullable(userEntityThird));
        UserResponse expected = new UserResponse(3,
                "name3",
                "3@mail.ru",
                "EN",
                List.of(bankEntityThird.getName()));
        when(mapper.userEntityToUserResponse(userEntityThird)).thenReturn(expected);
        final UserResponse actual = userService.getUserById(3);

        assertEquals(expected, actual);
    }

    @Test
    void testGetUserByNotExistingUserId() {
        when(userDao.selectUserById(4)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(4));
    }

    @Test
    void testUpdateUser() {
        UserModifyRequest userModifyRequest = new UserModifyRequest(1, "newName", "newEmail", "newSex");
        assertDoesNotThrow(() -> userService.updateUser(userModifyRequest, 1));
    }

    @Test
    void testUpdateUserWithWrongPathVariableId() {
        UserModifyRequest userModifyRequest = new UserModifyRequest(1, "newName", "newEmail", "newSex");
        assertThrows(BadRequestException.class, () -> userService.updateUser(userModifyRequest, 2));
    }

    @Test
    void testUpdateUserWithNullName() {
        UserModifyRequest userModifyRequest = new UserModifyRequest(1, null, "newEmail", "newSex");
        assertThrows(BadRequestException.class, () -> userService.updateUser(userModifyRequest, 1));
    }

    @Test
    void testUpdateUserWithNullEmail() {
        UserModifyRequest userModifyRequest = new UserModifyRequest(1, "name", null, "newSex");
        assertThrows(BadRequestException.class, () -> userService.updateUser(userModifyRequest, 1));
    }

    @Test
    void testUpdateUserWithNotExistingUserId() {
        UserModifyRequest userModifyRequest = new UserModifyRequest(4, "newName", "newEmail", "newSex");
        doThrow(new UserNotFoundException(4)).when(userDao).updateUser(any(), eq(4));
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userModifyRequest, 4));
    }

    @Test
    void testDeleteUserById() {
        when(userDao.deleteUserById(3)).thenReturn(true);
        assertDoesNotThrow(() -> userService.deleteUserById(3));
    }

    @Test
    void testDeleteUserByWrongId() {
        when(userDao.deleteUserById(4)).thenThrow(new UserNotFoundException(4));
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(4));
    }
}