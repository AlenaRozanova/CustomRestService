package com.example.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankResponse {
    private int id;

    private String name;
    @SerializedName("country_name")
    private String countryName;

    @SerializedName("users_name")
    private List<String> usersName;

    public BankResponse() {
    }

    public BankResponse(final int id, final String name, final String countryName, final List<String> usersName) {
        this.id = id;
        this.name = name;
        this.countryName = countryName;
        this.usersName = usersName;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<String> getUsersName() {
        return usersName;
    }

    public void setUsersName(List<String> usersName) {
        this.usersName = usersName;
    }
}
