package com.example.validator;

import com.example.requset.UserBankRequest;

import java.util.Collection;
import java.util.List;

public class UserBankValidator {
    public boolean validate(final UserBankRequest userBankRequest) {
        final List<Integer> banksId = userBankRequest.getBanksId();
        return userBankRequest.getUserId() >= 0 &&
               banksId != null && !banksId.isEmpty() && !hasDuplicates(userBankRequest.getBanksId());
    }

    private boolean hasDuplicates(Collection<?> objects) {
        return objects.stream().distinct().count() != objects.size();
    }
}
