package com.example.requset;

import com.google.gson.annotations.SerializedName;

public class CountryInsertRequest {
    @SerializedName("name")
    private String name;

    public CountryInsertRequest() {
    }

    public CountryInsertRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
