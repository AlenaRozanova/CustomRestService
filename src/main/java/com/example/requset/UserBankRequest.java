package com.example.requset;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserBankRequest {
    @SerializedName("user_id")
    private int userId;
    @SerializedName("banks_id")
    private List<Integer> banksId;

    public UserBankRequest(final int userId, final List<Integer> banksId) {
        this.userId = userId;
        this.banksId = banksId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }

    public List<Integer> getBanksId() {
        return banksId;
    }

    public void setBanksId(final List<Integer> banksId) {
        this.banksId = banksId;
    }
}
