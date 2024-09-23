package com.example.data.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BankEntity {

    private int id;
    private String name;

    private Set<UserEntity> userEntities;

    private CountryEntity country;

    public BankEntity() {
    }

    public BankEntity(final Integer id, final String name) {
        this.id = id;
        this.name = name;
        this.userEntities = new HashSet<>();
    }

    public BankEntity(final int id, final String name, final CountryEntity country) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.userEntities = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserEntities(Set<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public Set<UserEntity> getUserEntities() {
        return userEntities;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BankEntity that = (BankEntity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(userEntities, that.userEntities) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String toShortString() {
        return "BankEntity{" +
               "id=" + id +
               ", name='" + name +
               '}';
    }

    @Override
    public String toString() {
        return "BankEntity{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", userBankEntities=" +
               userEntities.stream()
                       .map(UserEntity::toShortString)
                       .collect(Collectors.joining(", ")) +
               ", country=" + country.toShortString() +
               '}';
    }
}
