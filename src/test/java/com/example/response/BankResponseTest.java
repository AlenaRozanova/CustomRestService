package com.example.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankResponseTest {
    @Test
    void errorTest() {
        int id = 1;
        String name = "name";
        List<String> usersName = List.of("name1", "name2");
        String countryName = "countryName";
        BankResponse bankResponse = new BankResponse(id, name, countryName, usersName);

        assertEquals(bankResponse.getId(), id);
        assertEquals(bankResponse.getName(), name);
        assertEquals(bankResponse.getUsersName(), usersName);
        assertEquals(bankResponse.getCountryName(), countryName);

        int id2 = 2;
        String name2 = "name2";
        List<String> usersName2 = List.of("name3", "name4");
        String countryName2 = "countryName2";
        bankResponse.setId(id2);
        bankResponse.setName(name2);
        bankResponse.setUsersName(usersName2);
        bankResponse.setCountryName(countryName2);

        assertEquals(bankResponse.getId(), id2);
        assertEquals(bankResponse.getName(), name2);
        assertEquals(bankResponse.getUsersName(), usersName2);
        assertEquals(bankResponse.getCountryName(), countryName2);
    }
}
