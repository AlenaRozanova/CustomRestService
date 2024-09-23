package com.example.requset;

import com.google.gson.annotations.SerializedName;

public class BankModifyRequest {
    private int id;
    private String name;

    @SerializedName("country_id")
    private int countryId;

    public BankModifyRequest() {
    }

    public BankModifyRequest(int id, String name, int countryId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
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

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
