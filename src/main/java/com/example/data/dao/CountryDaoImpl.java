package com.example.data.dao;

import com.example.data.mapper.CountryResultSetMapper;
import com.example.data.model.CountryEntity;
import com.example.database.connection.ConnectionManager;
import com.example.exception.CountryNotFoundException;
import com.example.exception.SQLRuntimeException;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CountryDaoImpl implements CountryDao {
    private static final Logger log = LogManager.getLogger(CountryDaoImpl.class);

    private static final String INSERT_AUTHOR = "INSERT INTO countries (name) VALUES (?)";
    private static final String UPDATE_AUTHOR = "UPDATE countries SET name = ? WHERE id = ?";
    private static final String DELETE_AUTHOR = "DELETE FROM countries WHERE id = ?";
    private static final String SELECT_AUTHOR_BY_ID =
            """
                    SELECT id, name
                    FROM countries a
                    WHERE id = ?
                    """;
    private static final String COUNT_AUTHOR_BY_ID = """
            SELECT count(*) count
            FROM countries a
            WHERE a.id = ?
            """;

    private final ConnectionManager connectionManager;
    private final BankDao bankDao;

    private final CountryResultSetMapper countryEntityResultSetMapper;

    @Inject
    public CountryDaoImpl(final ConnectionManager connectionManager, final BankDao bankDao) {
        this.connectionManager = connectionManager;
        this.bankDao = bankDao;
        this.countryEntityResultSetMapper = new CountryResultSetMapper(bankDao);
    }

    @Override
    public boolean insertCountry(final CountryEntity country) {
        boolean result;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_AUTHOR)) {
            int idx = 0;
            preparedStatement.setString(++idx, country.getName());
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return result;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public boolean isExistById(final int id) {
        int count;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_AUTHOR_BY_ID);) {
            preparedStatement.setInt(1, id);
            log.info(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt("count");
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return count == 1;
    }

    @Override
    public Optional<CountryEntity> selectCountryById(final int id) {
        CountryEntity countryEntity;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_AUTHOR_BY_ID);) {
            preparedStatement.setInt(1, id);
            log.info(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) throw new CountryNotFoundException(id);
            countryEntity = countryEntityResultSetMapper.map(resultSet);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return Optional.ofNullable(countryEntity);
    }

    @Override
    public boolean updateCountry(final CountryEntity countryEntity, final int id) {
        // if country don't exist - throw exception
        if (!isExistById(id)) throw new CountryNotFoundException(id);
        boolean rowUpdated;
        // update country
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statementCountryUpdate = connection.prepareStatement(UPDATE_AUTHOR);) {
            int idx = 0;
            statementCountryUpdate.setString(++idx, countryEntity.getName());
            statementCountryUpdate.setInt(++idx, countryEntity.getId());
            rowUpdated = statementCountryUpdate.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return rowUpdated;
    }

    @Override
    public boolean deleteCountryById(final int id) {
        if (!isExistById(id)) throw new CountryNotFoundException(id);
        boolean rowDeleted;
        // delete banks associated with country
        bankDao.deleteBanksByCountryId(id);

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_AUTHOR);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return rowDeleted;
    }
}
