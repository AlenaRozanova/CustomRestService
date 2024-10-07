package com.example.response;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserResponseTest {
    @Test
    void errorTest() {
        int id = 1;
        String name = "name";
        int old = 20;
        String email = "email";
        String sex = "male";
        List<String> banksName = List.of("name1", "name2");
        UserResponse userResponse = new UserResponse(id, name, old, email, sex, banksName);

        assertEquals(userResponse.getBanksName(), banksName);
        assertEquals(userResponse.getEmail(), email);
        assertEquals(userResponse.getName(), name);
        assertEquals(userResponse.getId(), id);
        assertEquals(userResponse.getOld(), old);
        assertEquals(userResponse.getSex(), sex);

        int id2 = 1;
        String name2 = "name";
        int old2 = 20;
        String email2 = "email";
        String sex2 = "male";
        List<String> banksName2 = List.of("name1", "name2");
        userResponse.setBanksName(banksName2);
        userResponse.setEmail(email2);
        userResponse.setId(id2);
        userResponse.setBanksName(banksName2);
        userResponse.setSex(sex2);

        assertEquals(userResponse.getBanksName(), banksName2);
        assertEquals(userResponse.getEmail(), email2);
        assertEquals(userResponse.getName(), name2);
        assertEquals(userResponse.getId(), id2);
        assertEquals(userResponse.getOld(), old2);
        assertEquals(userResponse.getSex(), sex2);
    }
}
