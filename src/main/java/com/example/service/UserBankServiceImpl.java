package com.example.service;

import com.example.data.dao.UserBankDao;
import com.example.exception.BadRequestException;
import com.example.requset.UserBankRequest;
import com.example.validator.UserBankValidator;
import com.google.inject.Inject;

/**
 * Provides methods to add and delete banks from a user
 */
public class UserBankServiceImpl implements UserBankService {
    private final UserBankDao userBankDao;

    private final UserBankValidator userBankValidator;

    /**
     * Constructor for UserBankServiceImpl.
     *
     * @param userBankDao       UserBankDao instance.
     * @param userBankValidator UserBankValidator instance.
     */
    @Inject
    public UserBankServiceImpl(final UserBankDao userBankDao, final UserBankValidator userBankValidator) {
        this.userBankDao = userBankDao;
        this.userBankValidator = userBankValidator;
    }

    /**
     * Adds banks to a user.
     *
     * @param request UserBankRequest instance containing the list of banks and user ID.
     * @throws BadRequestException if the request is invalid.
     */
    @Override
    public void addBanksToUser(final UserBankRequest request) {
        if (!userBankValidator.validate(request))
            throw new BadRequestException("Invalid users banks add request");
        userBankDao.insertUserBanksByUserId(request.getBanksId(), request.getUserId());
    }

    /**
     * Deletes a banks from a user
     *
     * @param request UserBankRequest instance containing the list of banks and user ID.
     * @throws BadRequestException if the request is invalid.
     */
    @Override
    public void deleteBankFromUser(final UserBankRequest request) {
        if (!userBankValidator.validate(request))
            throw new BadRequestException("Invalid users banks delete request");
        userBankDao.deleteUserBanksByUserId(request.getBanksId(), request.getUserId());
    }
}
