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

    public String getName() {
        return name;
    }

    public int getCountryId() {
        return countryId;
    }

}
