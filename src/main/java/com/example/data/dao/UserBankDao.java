package com.example.data.dao;

import java.util.Collection;

public interface UserBankDao {

    boolean insertUserBanksByUserId(final Collection<Integer> banksId, final int userId);

    boolean deleteUserBanksByUserId(final Collection<Integer> banksId, final int userId);

    boolean deleteUserBanksByBankId(final int bankId);

    boolean deleteUserBanksByUserId(final int userId);
}
