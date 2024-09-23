package com.example.data.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class CountryEntity {
    private int id;

    private String name;
    private Set<BankEntity> bankEntities;

    public CountryEntity() {
        this.bankEntities = new HashSet<>();
    }

    public CountryEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.bankEntities = new HashSet<>();
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

    public Set<BankEntity> getBankEntities() {
        return bankEntities;
    }

    public void setBankEntities(Set<BankEntity> bankEntities) {
        this.bankEntities = bankEntities;
    }

    public String toShortString() {
        return "CountryEntity{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }

    @Override
    public String toString() {
        return "CountryEntity{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", bankEntities=" + bankEntities.stream().map(BankEntity::toShortString).collect(Collectors.joining(", ")) +
               '}';
    }
}
