package com.example.data.dao;

import com.example.data.model.BankEntity;
import com.example.database.connection.ConnectionManager;
import com.example.exception.BadRequestException;
import com.example.exception.BankNotFoundException;
import com.example.exception.SQLRuntimeException;
import com.example.exception.UserNotFoundException;
import com.example.util.StringUtils;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * UserBankDaoImpl class provides methods to manage the relationship between users and banks.
 */
public class UserBankDaoImpl implements UserBankDao {
    private static final Logger log = LogManager.getLogger(UserBankDaoImpl.class);
    private static final String DELETE_USER_BOOKS_BY_USER_ID_AND_BOOKS_IDS = """
            DELETE FROM banks_users
            WHERE bank_id in (%s) AND user_id = ?
            """;

    private static final String DELETE_USER_BOOKS_BY_USER_ID = """
            DELETE FROM banks_users
            WHERE user_id = ?
            """;

    private static final String DELETE_USER_BOOKS_BY_BOOK_ID = """
            DELETE FROM banks_users
            WHERE bank_id = ?
            """;

    private static final String INSERT_USER_BOOK = "INSERT INTO banks_users (user_id, bank_id) VALUES (?, ?)";
    private final ConnectionManager connectionManager;
    private final UserDao userDao;
    private final BankDao bankDao;

    /**
     * Constructor for UserBankDaoImpl.
     *
     * @param connectionManager Data source instance for interacting with the database.
     * @param userDao           UserDao instance for interacting with the users table.
     * @param bankDao           BankDao instance for interacting with the banks table.
     */
    @Inject
    public UserBankDaoImpl(final ConnectionManager connectionManager, final UserDao userDao, final BankDao bankDao) {
        this.connectionManager = connectionManager;
        this.userDao = userDao;
        this.bankDao = bankDao;
    }

    /**
     * Add specified banks to be owned by user.
     *
     * @param banksId banks ids that will be owned by user after query
     * @param userId  user id to which banks will be added
     * @return true if database has been changed
     */
    @Override
    public boolean insertUserBanksByUserId(final Collection<Integer> banksId, final int userId) {
        checkParameters(banksId, userId);
        // find if user already have links to banks
        final Set<BankEntity> bankEntities = bankDao.selectBanksByUserId(userId);
        final Set<Integer> old = bankEntities.stream().map(BankEntity::getId).collect(Collectors.toSet());
        Set<Integer> intersection = new HashSet<>(banksId);
        intersection.retainAll(old);
        if (!intersection.isEmpty()) {
            throw new BadRequestException(MessageFormat
                    .format("user already have link to bank(s) with id ({0})",
                            StringUtils.collectionToSingleString(intersection, ", ")));
        }

        if (banksId.isEmpty()) return false;
        boolean rowInserted = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER_BOOK);) {
            log.info(INSERT_USER_BOOK);
            statement.setInt(1, userId);
            for (int bankId : banksId) {
                statement.setInt(2, bankId);
                statement.addBatch();
            }
            int[] rowInsertedCntArray = statement.executeBatch();
            for (int i : rowInsertedCntArray) {
                if (i > 0) {
                    rowInserted = true;
                    break;
                }
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return rowInserted;
    }

    /**
     * Delete specified banks owned by user.
     *
     * @param banksId ids that will be not more owned by user
     * @param userId  user id from which banks will be deleted
     * @return true if database has been changed
     */
    @Override
    public boolean deleteUserBanksByUserId(final Collection<Integer> banksId, final int userId) {
        if (banksId.isEmpty()) return false;
        checkParameters(banksId, userId);
        // find if user don't have links to banks
        final Set<BankEntity> bankEntities = bankDao.selectBanksByUserId(userId);
        final Set<Integer> old = bankEntities.stream().map(BankEntity::getId).collect(Collectors.toSet());
        Set<Integer> intersection = new HashSet<>(banksId);
        intersection.removeAll(old);
        if (!intersection.isEmpty()) {
            throw new BadRequestException(MessageFormat
                    .format("User dont have link to bank(s) with id ({0})",
                            StringUtils.collectionToSingleString(intersection, ", ")));
        }
        String banksIdString = StringUtils.collectionToSingleString(banksId, ", ");
        String query = String.format(DELETE_USER_BOOKS_BY_USER_ID_AND_BOOKS_IDS, banksIdString);
        return deleteById(userId, query);
    }

    /**
     * Deletes all banks owned by the specified user.
     *
     * @param userId the id of the user whose owned banks will be deleted
     * @return true if the database has been changed, false otherwise
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    public boolean deleteUserBanksByUserId(final int userId) {
        if (!userDao.isExistById(userId)) throw new UserNotFoundException(userId);
        return deleteById(userId, DELETE_USER_BOOKS_BY_USER_ID);
    }

    /**
     * Deletes all banks with specified if from users
     *
     * @param bankId the id of the bank to be deleted from users
     * @return true if the database has been changed, false otherwise
     * @throws BankNotFoundException if bank does not exist
     */
    @Override
    public boolean deleteUserBanksByBankId(final int bankId) {
        if (!bankDao.isExistById(bankId)) throw new BankNotFoundException(bankId);
        return deleteById(bankId, DELETE_USER_BOOKS_BY_BOOK_ID);
    }

    /**
     * Checks if the specified parameters are valid.
     *
     * @param banksId the ids of the banks
     * @param userId  the id of the user
     */
    private void checkParameters(final Collection<Integer> banksId, final int userId) {
        if (!userDao.isExistById(userId)) throw new UserNotFoundException(userId);
        if (!bankDao.isBanksExistById(banksId))
            throw new BankNotFoundException(MessageFormat
                    .format("At least one bank with id from array {1} dont exist!",
                            userId, banksId));
    }

    /**
     * Deletes a record from the database based on the specified id and query.
     *
     * @param id          the id of the record to be deleted
     * @param deleteQuery the SQL query to delete the record
     * @return true if the database has been changed, false otherwise
     */
    private boolean deleteById(int id, final String deleteQuery) {
        boolean rowDeleted;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery);) {
            log.info(deleteQuery);
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLRuntimeException(e.getMessage());
        }
        return rowDeleted;
    }
}
