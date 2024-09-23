package com.example.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class CountryResponse {
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("banks_name")
    private List<String> banksName;

    public CountryResponse() {
    }

    public CountryResponse(final int id,
                          final String name,
                          final List<String> banksName) {
        this.id = id;
        this.name = name;
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

    public void setName(final String name) {
        this.name = name;
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
        final CountryResponse that = (CountryResponse) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(banksName, that.banksName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
