package com.example.response;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class UserResponse {
    private int id;
    private String name;
    private int old;
    private String email;
    private String sex;

    @SerializedName("banks_name")
    private List<String> banksName;

    public UserResponse() {
    }

    public UserResponse(int id, String name, int old, String email, String sex, List<String> banksName) {
        this.id = id;
        this.name = name;
        this.old = old;
        this.email = email;
        this.sex = sex;
        this.banksName = banksName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOld() {
        return old;
    }

    public void setOld(int old) {
        this.old = old;
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

    public List<String> getBanksName() {
        return banksName;
    }

    public void setBanksName(List<String> banksName) {
        this.banksName = banksName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UserResponse that = (UserResponse) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(old, that.old) && Objects.equals(email, that.email) && Objects.equals(sex, that.sex) && Objects.equals(banksName, that.banksName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
               "id=" + id +
               ", name='" + name + '\'' +
                ", old='" + old + '\'' +
                ", email='" + email + '\'' +
               ", sex='" + sex + '\'' +
               ", banksName=" + banksName +
               '}';
    }
}
