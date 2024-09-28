package com.example.data.mapper;

import com.example.data.dao.BankDao;
import com.example.data.model.UserEntity;
import com.google.inject.Inject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResultSetMapper {
    private final BankDao bankDao;

    @Inject
    public UserResultSetMapper(BankDao bankDao) {
        this.bankDao = bankDao;
    }

    public UserEntity map(ResultSet resultSet) throws SQLException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(resultSet.getInt("id"));
        userEntity.setName(resultSet.getString("name"));
        userEntity.setOld(resultSet.getInt("old"));
        userEntity.setSex(resultSet.getString("sex"));
        userEntity.setEmail(resultSet.getString("email"));
        userEntity.setBankEntitySet(bankDao.selectBanksByUserId(userEntity.getId()));
        return userEntity;
    }

    public UserEntity getUserByBankId(ResultSet resultSet) throws SQLException {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(resultSet.getInt("id"));
        userEntity.setName(resultSet.getString("name"));
        userEntity.setOld(resultSet.getInt("old"));
        userEntity.setEmail(resultSet.getString("email"));
        userEntity.setSex(resultSet.getString("sex"));
        return userEntity;
    }
}
