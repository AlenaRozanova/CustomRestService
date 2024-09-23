package com.example.validator;

import com.example.requset.BankInsertRequest;
import com.example.requset.BankModifyRequest;

public class BankValidator {
    public boolean validate(final BankInsertRequest bankInsertRequest) {
        final String bankName = bankInsertRequest.getName();
        return bankInsertRequest.getCountryId() >= 0 && bankName != null && !bankName.isEmpty();
    }

    public boolean validate(final BankModifyRequest bankModifyRequest, final int id) {
        final String bankName = bankModifyRequest.getName();
        return id >= 0 && bankName != null && !bankName.isEmpty() && bankModifyRequest.getId() == id;
    }
}
