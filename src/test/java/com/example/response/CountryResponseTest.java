package com.example.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountryResponseTest {
    @Test
    void errorTest() {
        int id = 1;
        String name = "servletName";
        List<String> banksName = List.of("name1", "name2");
        CountryResponse countryResponse = new CountryResponse(id, name, banksName);

        assertEquals(countryResponse.getId(), id);
        assertEquals(countryResponse.getName(), name);
        assertEquals(countryResponse.getBanksName(), banksName);

        int id2 = 2;
        String name2 = "name2";
        List<String> banksName2 = List.of("name3", "name4");
        countryResponse.setId(id2);
        countryResponse.setName(name2);
        countryResponse.setBanksName(banksName2);

        assertEquals(countryResponse.getId(), id2);
        assertEquals(countryResponse.getName(), name2);
        assertEquals(countryResponse.getBanksName(), banksName2);
    }
}
