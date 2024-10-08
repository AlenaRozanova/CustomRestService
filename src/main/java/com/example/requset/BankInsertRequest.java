package com.example.requset;

import com.google.gson.annotations.SerializedName;

public class BankInsertRequest {
    private String name;

    @SerializedName("country_id")
    private int countryId;

    public BankInsertRequest() {
    }

    public BankInsertRequest(String name, int countryId) {
        this.name = name;
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public int getCountryId() {
        return countryId;
    }
}
