package com.example.service;

import com.example.requset.BankInsertRequest;
import com.example.requset.BankModifyRequest;
import com.example.response.BankResponse;

public interface BankService {
    void addBank(final BankInsertRequest bankInsertRequest);

    BankResponse getBankById(final int id);

    void updateBank(final BankModifyRequest bankModifyRequest, final int id);

    void deleteBankById(final int id);
}
