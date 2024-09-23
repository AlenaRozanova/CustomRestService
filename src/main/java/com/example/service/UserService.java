package com.example.service;

import com.example.requset.UserInsertRequest;
import com.example.requset.UserModifyRequest;
import com.example.response.UserResponse;

public interface UserService {
    void addUser(final UserInsertRequest user);

    UserResponse getUserById(final int id);

    void updateUser(final UserModifyRequest user, final int id);

    void deleteUserById(final int id);

}
