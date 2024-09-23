package com.example.data.mapper;

import com.example.data.dao.BankDao;
import com.example.data.model.CountryEntity;
import com.google.inject.Inject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryResultSetMapper {
    private final BankDao bankDao;

    @Inject
    public CountryResultSetMapper(BankDao bankDao) {
        this.bankDao = bankDao;
    }

    public CountryEntity map(ResultSet resultSet) throws SQLException {
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(resultSet.getInt("id"));
        countryEntity.setName(resultSet.getString("name"));

        countryEntity.setBankEntities(bankDao.selectBanksByCountryId(countryEntity.getId()));
        return countryEntity;
    }
}
