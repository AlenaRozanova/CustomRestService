package com.example.data.model;

public class UserBankEntity {
    private BankEntity bank;
    private UserEntity user;

    public UserBankEntity() {
    }

    public UserBankEntity(BankEntity bank, UserEntity user) {
        this.bank = bank;
        this.user = user;
    }

    public BankEntity getBank() {
        return bank;
    }

    public void setBank(BankEntity bank) {
        this.bank = bank;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String toShortString() {
        return "UserBankEntity{" +
               "bank=" + bank.getName() +
               ", user=" + user.getName() +
               '}';
    }

    @Override
    public String toString() {
        return "UserBankEntity{" +
               "bank=" + bank.toShortString() +
               ", user=" + user.toShortString() +
               '}';
    }
}
