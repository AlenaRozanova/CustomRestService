package com.example.service;

import com.example.requset.UserBankRequest;

public interface UserBankService {

    void addBanksToUser(final UserBankRequest request);

    void deleteBankFromUser(final UserBankRequest request);

}
