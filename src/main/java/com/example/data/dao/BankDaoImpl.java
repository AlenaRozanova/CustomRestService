package com.example.data.dao;

import com.example.data.mapper.BankResultSetMapper;
import com.example.data.model.BankEntity;
import com.example.database.connection.ConnectionManager;
import com.example.exception.BankNotFoundException;
import com.example.exception.CountryNotFoundException;
import com.example.exception.SQLRuntimeException;
import com.example.exception.UserNotFoundException;
import com.example.util.StringUtils;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Dao for user CRUD operations
 */
public class BankDaoImpl implements BankDao {
    private static final Logger log = LogManager.getLogger(BankDaoImpl.class);
    private static final String SELECT_BOOK_BY_ID = "SELECT id, name, country_id FROM banks WHERE id = ?";
    private static final String INSERT_BOOK = "INSERT INTO banks (name, country_id) VALUES (?, ?)";
    private static final String UPDATE_BOOK = "UPDATE banks SET name = ?, country_id= ? WHERE id = ?";
    private static final String DELETE_BOOK = "DELETE FROM banks WHERE id = ?";
    private static final String COUNT_BOOK_BY_ID = """
            SELECT count(*) count
            FROM banks b
            WHERE b.id = ?
            """;
    private static final String COUNT_BOOKS_BY_ID = """
            SELECT count(*) count
            FROM banks b
            WHERE b.id in (%s)
            """;
    private static final String SELECT_BOOKS_BY_AUTHOR_ID =
            """
                    SELECT id, name, country_id
                    FROM banks
                    WHERE country_id = ?
                            """;

    private static final String SELECT_BOOKS_BY_USER_ID = """
            SELECT id, name, user_id
            FROM banks b
                INNER JOIN banks_users bu ON ( b.id = bu.bank_id  )
            WHERE bu.user_id = ?
            """;

    private final ConnectionManager connectionManager;

    private final BankResultSetMapper bankEntityResultSetMapper;

    private final CountryDao countryDao;

    private final UserBankDao userBankDao;
    private final UserDao userDao;

    /**
     * Initializes the {@code BankDaoImpl} with the provided dependencies.
     *
     * @param connectionManager the {@link ConnectionManager} to be used for database operations
     * @param userDao           the {@link UserDao} to be used for user-related operations
     * @param countryDao         the {@link CountryDao} to be used for country-related operations
     * @param userBankDao       the {@link UserBankDao} to be used for user-bank-related operations
     */
    @Inject
    public BankDaoImpl(final ConnectionManager connectionManager,
                       final UserDao userDao,
                       final CountryDao countryDao,
                       final UserBankDao userBankDao) {
        this.connectionManager = connectionManager;
        this.countryDao = countryDao;
        this.userBankDao = userBankDao;
        this.userDao = userDao;
        this.bankEntityResultSetMapper = new BankResultSetMapper(userDao, countryDao);
    }

    /**
     * Inserts a new bank into the database.
     *
     * @param bankEntity the {@link BankEntity} to be inserted
     * @return true if the insertion was successful, false otherwise
     * @throws CountryNotFoundException if the country of the bank does not exist in the database
     * @throws SQLRuntimeException     if an error occurs during the database operation
     */
    public boolean insertBank(final BankEntity bankEntity) {
        if (!countryDao.isExistById(bankEntity.getCountry().getId()))
            throw new CountryNotFoundException(bankEntity.getCountry().getId());
        boolean result;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BOOK)) {
            int idx = 0;
            preparedStatement.setString(++idx, bankEntity.getName());
            preparedStatement.setInt(++idx, bankEntity.getCountry().getId());
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return result;
    }

    /**
     * Retrieves a bank by its id from the database.
     *
     * @param id the id of the bank to be retrieved
     * @return an {@link Optional} containing the {@link BankEntity} if found, otherwise an empty {@link Optional}
     * @throws SQLRuntimeException if an error occurs during the database operation
     */
    public Optional<BankEntity> selectBankById(final int id) {
        BankEntity bankEntity = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_BY_ID);) {
            preparedStatement.setInt(1, id);
            log.info(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return Optional.empty();
            bankEntity = bankEntityResultSetMapper.map(resultSet);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return Optional.ofNullable(bankEntity);
    }

    /**
     * Retrieves all banks associated with a specific country.
     *
     * @param countryId the id of the country whose banks should be retrieved
     * @return a set of {@link BankEntity} objects if found, otherwise an empty set
     * @throws SQLRuntimeException     if an error occurs during the database operation
     * @throws CountryNotFoundException if country not exist
     */
    @Override
    public Set<BankEntity> selectBanksByCountryId(final int countryId) {
        if (!countryDao.isExistById(countryId)) throw new CountryNotFoundException(countryId);
        Set<BankEntity> bankEntitySet = new HashSet<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOKS_BY_AUTHOR_ID);) {
            preparedStatement.setInt(1, countryId);
            log.info(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                bankEntitySet.add(bankEntityResultSetMapper.getBanksById(resultSet));
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return bankEntitySet;
    }

    /**
     * Retrieves all banks associated with a specific user.
     *
     * @param userId the id of the user whose banks should be retrieved
     * @return a set of {@link BankEntity} objects if found, otherwise an empty set
     * @throws SQLRuntimeException if an error occurs during the database operation
     */
    @Override
    public Set<BankEntity> selectBanksByUserId(final int userId) {
        if (!userDao.isExistById(userId)) throw new UserNotFoundException(userId);
        Set<BankEntity> bankEntitySet = new HashSet<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOKS_BY_USER_ID);) {
            preparedStatement.setInt(1, userId);
            log.info(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                bankEntitySet.add(bankEntityResultSetMapper.getBanksById(resultSet));
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return bankEntitySet;
    }

    /**
     * Checks if a bank with the given id exists in the database.
     *
     * @param bankId the id of the bank to be checked
     * @return true if the bank exists, false otherwise
     * @throws SQLRuntimeException if an error occurs during the database operation
     */
    public boolean isExistById(final int bankId) {
        int count;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_BOOK_BY_ID);) {
            preparedStatement.setInt(1, bankId);
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
    public boolean isBanksExistById(final Collection<Integer> banksId) {
        if (banksId.isEmpty()) return true;
        int count;
        String banksIdString = StringUtils.collectionToSingleString(banksId, ", ");
        String query = String.format(COUNT_BOOKS_BY_ID, banksIdString);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            log.info(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt("count");
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return count == banksId.size();
    }

    /**
     * Updates a bank in the database.
     *
     * @param bankEntity the {@link BankEntity} to be updated
     * @param id         the id of the bank to be updated
     * @return true if the update was successful, false otherwise
     * @throws CountryNotFoundException if the country of the bank does not exist in the database
     * @throws SQLRuntimeException     if an error occurs during the database operation
     */
    public boolean updateBank(final BankEntity bankEntity, final int id) {
        if (!countryDao.isExistById(bankEntity.getCountry().getId()))
            throw new CountryNotFoundException(bankEntity.getCountry().getId());
        if (!isExistById(id)) throw new BankNotFoundException(id);
        boolean rowUpdated;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statementBankUpdate = connection.prepareStatement(UPDATE_BOOK);) {
            int idx = 0;
            statementBankUpdate.setString(++idx, bankEntity.getName());
            statementBankUpdate.setInt(++idx, bankEntity.getCountry().getId());
            statementBankUpdate.setInt(++idx, bankEntity.getId());
            rowUpdated = statementBankUpdate.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return rowUpdated;
    }

    /**
     * Deletes a bank by its id from the database.
     *
     * @param id the id of the bank to be deleted
     * @return true if the deletion was successful, false otherwise
     * @throws SQLRuntimeException if an error occurs during the database operation
     */
    public boolean deleteBankById(final int id) {
        if (!isExistById(id)) throw new BankNotFoundException(id);
        boolean rowDeleted;
        userBankDao.deleteUserBanksByBankId(id);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BOOK);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return rowDeleted;
    }

    /**
     * Deletes all banks associated with a specific country.
     *
     * @param countryId the id of the country whose banks should be deleted
     * @return true if all banks were successfully deleted, false otherwise
     * @throws CountryNotFoundException if country not exist
     */
    @Override
    public boolean deleteBanksByCountryId(final int countryId) {
        if (!countryDao.isExistById(countryId)) throw new CountryNotFoundException(countryId);
        boolean result = true;
        final Set<BankEntity> bankEntities = selectBanksByCountryId(countryId);
        for (final BankEntity bankEntity : bankEntities) {
            result &= deleteBankById(bankEntity.getId());
        }
        return result;
    }
}
