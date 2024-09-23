package com.example.requset;

import com.google.gson.annotations.SerializedName;

public class CountryModifyRequest {
    private int id;
    @SerializedName("name")
    private String name;

    public CountryModifyRequest() {
    }

    public CountryModifyRequest(final int id, final String name) {
        this.id = id;
        this.name = name;
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

    public void setName(final String name) {
        this.name = name;
    }
}
