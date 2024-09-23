package com.example.data.dao;

import com.example.data.model.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface UserDao {

    boolean insertUser(final UserEntity user);

    Optional<UserEntity> selectUserById(final int id);

    boolean updateUser(final UserEntity user, final int id);

    boolean deleteUserById(final int id);

    Set<UserEntity> selectUsersByBankId(final int bankId);

    boolean isExistById(final int id);

}
