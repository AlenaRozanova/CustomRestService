package com.example.data.mapper;

import com.example.data.dao.CountryDao;
import com.example.data.dao.UserDao;
import com.example.data.model.BankEntity;
import com.example.exception.CountryNotFoundException;
import com.google.inject.Inject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BankResultSetMapper {
    private final UserDao userDao;
    private final CountryDao countryDao;

    @Inject
    public BankResultSetMapper(final UserDao userDao, final CountryDao countryDao) {
        this.userDao = userDao;
        this.countryDao = countryDao;
    }

    public BankEntity map(final ResultSet resultSet) throws SQLException {
        BankEntity bankEntity = new BankEntity();
        bankEntity.setId(resultSet.getInt("id"));
        bankEntity.setName(resultSet.getString("name"));
        int countryId = resultSet.getInt("country_id");
        bankEntity.setCountry(countryDao.selectCountryById(countryId)
                .orElseThrow(() -> new CountryNotFoundException(countryId)));
        bankEntity.setUserEntities(userDao.selectUsersByBankId(bankEntity.getId()));
        return bankEntity;
    }

    public BankEntity getBanksById(final ResultSet resultSet) throws SQLException {
        BankEntity bank = new BankEntity();
        bank.setId(resultSet.getInt("id"));
        bank.setName(resultSet.getString("name"));
        return bank;
    }
}
