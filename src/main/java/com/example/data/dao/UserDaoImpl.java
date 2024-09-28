package com.example.data.dao;

import com.example.data.mapper.UserResultSetMapper;
import com.example.data.model.UserEntity;
import com.example.database.connection.ConnectionManager;
import com.example.exception.BankNotFoundException;
import com.example.exception.SQLRuntimeException;
import com.example.exception.UserNotFoundException;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Dao for user CRUD operations
 */
public class UserDaoImpl implements UserDao {
    Logger log = LogManager.getLogger(UserDaoImpl.class);
    private static final String INSERT_USER = "INSERT INTO users (name, old, email, sex) VALUES (?, ?, ?, ?)";

    private static final String SELECT_USER_BY_ID = "SELECT id, name, old, email, sex FROM users WHERE id =?";

    private static final String SELECT_USERS_BY_BOOK_ID = """
            SELECT id, name, old, email, sex
            FROM users u
            JOIN banks_users bu ON (u.id = bu.user_id)
            WHERE bu.bank_id = ?
            """;
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE users SET name = ?, old = ?, email= ?, sex =? WHERE id = ?";

    private static final String COUNT_USER_BY_ID = """
            SELECT count(*) count
            FROM users u
            WHERE u.id = ?
            """;

    private final ConnectionManager connectionManager;
    private final UserBankDao userBankDao;
    private final UserResultSetMapper userEntityResultSetMapper;
    private final BankDao bankDao;

    @Inject
    public UserDaoImpl(final ConnectionManager connectionManager, final BankDao bankDao, final UserBankDao userBankDao) {
        this.connectionManager = connectionManager;
        this.userBankDao = userBankDao;
        this.bankDao = bankDao;
        this.userEntityResultSetMapper = new UserResultSetMapper(bankDao);
    }

    /**
     * Insert a new user entity into the users table.
     *
     * @param userEntity The user entity object to be added to the database.
     * @throws SQLRuntimeException if an error occurs while executing the SQL query.
     */
    public boolean insertUser(final UserEntity userEntity) {
        boolean result;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
            int idx = 0;
            preparedStatement.setString(++idx, userEntity.getName());
            preparedStatement.setInt(++idx, userEntity.getOld());
            preparedStatement.setString(++idx, userEntity.getEmail());
            preparedStatement.setString(++idx, userEntity.getSex());
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return result;
    }

    /**
     * Retrieve a user entity by its id.
     *
     * @param id The id of the user to be retrieved.
     * @return An optional containing the user entity if found, otherwise an empty optional.
     * @throws SQLRuntimeException if an error occurs while executing the SQL query.
     */
    public Optional<UserEntity> selectUserById(final int id) {
        UserEntity userEntity = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            log.info(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                userEntity = userEntityResultSetMapper.map(resultSet);
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return Optional.ofNullable(userEntity);
    }

    /**
     * Delete a user from the database if it exists.
     *
     * @param id The id of the user to be deleted.
     * @return True if the user was successfully deleted, false otherwise.
     * @throws SQLRuntimeException   if an error occurs while executing the SQL query.
     * @throws UserNotFoundException if the user does not exist
     */
    public boolean deleteUserById(final int id) {
        if (!isExistById(id)) throw new UserNotFoundException(id);
        boolean rowDeleted;
        // Delete banks links in the link table banks_users
        userBankDao.deleteUserBanksByUserId(id);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return rowDeleted;
    }

    /**
     * Update a user's state specified by its id.
     *
     * @param userEntity The new user state.
     * @param id         The id of the user to be updated.
     * @return True if the database was successfully updated, false otherwise.
     * @throws SQLRuntimeException if an error occurs while executing the SQL query.
     */
    public boolean updateUser(final UserEntity userEntity, final int id) {
        if (!isExistById(id)) throw new UserNotFoundException(id);
        boolean rowUpdated;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statementUserUpdate = connection.prepareStatement(UPDATE_USER);) {
            int idx = 0;
            statementUserUpdate.setString(++idx, userEntity.getName());
            statementUserUpdate.setInt(++idx, userEntity.getOld());
            statementUserUpdate.setString(++idx, userEntity.getEmail());
            statementUserUpdate.setString(++idx, userEntity.getSex());
            statementUserUpdate.setInt(++idx, userEntity.getId());
            rowUpdated = statementUserUpdate.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return rowUpdated;
    }

    /**
     * Check if a user with the specified id exists in the database.
     *
     * @param id The id of the user to check.
     * @return True if a user with the specified id exists in the database, false otherwise.
     * @throws SQLRuntimeException if an error occurs while executing the SQL query.
     */
    public boolean isExistById(final int id) {
        int count;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_USER_BY_ID);) {
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

    /**
     * Retrieve users related to a specific bank id.
     *
     * @param bankId The id of the bank related to the users to be retrieved.
     * @return A set of user entities related to the specified bank id.
     * @throws SQLRuntimeException   if an error occurs while executing the SQL query.
     * @throws BankNotFoundException if bank with the specified id does not exist
     */
    @Override
    public Set<UserEntity> selectUsersByBankId(final int bankId) {
        if (!bankDao.isExistById(bankId)) throw new BankNotFoundException(bankId);
        Set<UserEntity> userEntitySet = new HashSet<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_BY_BOOK_ID);) {
            preparedStatement.setInt(1, bankId);
            log.info(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                userEntitySet.add(userEntityResultSetMapper.getUserByBankId(resultSet));
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return userEntitySet;
    }

}