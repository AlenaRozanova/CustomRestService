package com.example.validator;

import com.example.requset.UserInsertRequest;
import com.example.requset.UserModifyRequest;

public final class UserValidator {
    public boolean validate(final UserInsertRequest userInsertRequest) {
        return userInsertRequest.getName() != null
                && userInsertRequest.getOld() > 17
                && userInsertRequest.getEmail() != null
                && userInsertRequest.getSex() != null;
    }

    public boolean validate(final UserModifyRequest userModifyRequest, int id) {
        return id >= 0 && userModifyRequest.getId() == id
                && userModifyRequest.getName() != null
                && userModifyRequest.getOld() > 17
                && userModifyRequest.getEmail() != null
                && userModifyRequest.getSex() != null;
    }
}
