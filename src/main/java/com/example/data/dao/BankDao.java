package com.example.data.dao;

import com.example.data.model.BankEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface BankDao {
    boolean insertBank(final BankEntity bank);

    Optional<BankEntity> selectBankById(final int id);

    boolean updateBank(final BankEntity user, final int id);

    boolean deleteBankById(final int id);

    boolean deleteBanksByCountryId(int countryId);

    Set<BankEntity> selectBanksByCountryId(final int countryId);

    Set<BankEntity> selectBanksByUserId(final int userId);

    boolean isExistById(final int bankId);

    boolean isBanksExistById(final Collection<Integer> banksId);
}
