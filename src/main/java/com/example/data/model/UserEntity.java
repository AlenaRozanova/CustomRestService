package com.example.data.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User entity
 */
public class UserEntity {
    private int id;
    private String name;
    private String email;
    private String sex;

    private Set<BankEntity> bankEntitySet;

    public UserEntity() {
        this.bankEntitySet = new HashSet<>();
    }

    public UserEntity(String name, String email, String sex) {
        this.name = name;
        this.email = email;
        this.sex = sex;
        this.bankEntitySet = new HashSet<>();
    }

    public UserEntity(int id, String name, String email, String sex) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sex = sex;
        this.bankEntitySet = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<BankEntity> getBankEntitySet() {
        return bankEntitySet;
    }

    public void setBankEntitySet(Set<BankEntity> bankEntitySet) {
        this.bankEntitySet = bankEntitySet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
               Objects.equals(name, that.name) &&
               Objects.equals(email, that.email) &&
               Objects.equals(sex, that.sex) &&
               Objects.equals(bankEntitySet, that.bankEntitySet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, sex, bankEntitySet);
    }
}
