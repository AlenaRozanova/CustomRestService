package com.example;

import com.example.data.model.BankEntity;
import com.example.data.model.CountryEntity;
import com.example.data.model.UserEntity;
import org.junit.jupiter.api.BeforeAll;

import java.util.Set;

public class AbstractTest {
    protected static CountryEntity countryEntityFirst;
    protected static CountryEntity countryEntitySecond;
    protected static BankEntity bankEntityFirst;
    protected static BankEntity bankEntitySecond;
    protected static BankEntity bankEntityThird;
    protected static UserEntity userEntityFirst;
    protected static UserEntity userEntitySecond;
    protected static UserEntity userEntityThird;

    @BeforeAll
    static void setUp() {
        countryEntityFirst = new CountryEntity(1, "af1");
        countryEntitySecond = new CountryEntity(2, "af2");
        bankEntityFirst = new BankEntity(1, "bank 1", countryEntityFirst);
        bankEntitySecond = new BankEntity(2, "bank 2", countryEntitySecond);
        bankEntityThird = new BankEntity(3, "bank 3", countryEntityFirst);
        userEntityFirst = new UserEntity(1, "name1", "1@mail.ru", "male");
        userEntitySecond = new UserEntity(2, "name2", "2@mail.ru", "female");
        userEntityThird = new UserEntity(3, "name3", "3@mail.ru", "male");

        countryEntityFirst.setBankEntities(Set.of(bankEntityFirst, bankEntityThird));
        countryEntitySecond.setBankEntities(Set.of(bankEntitySecond));

        bankEntityFirst.setCountry(countryEntityFirst);
        bankEntitySecond.setCountry(countryEntityFirst);
        bankEntitySecond.setCountry(countryEntitySecond);
        bankEntityFirst.setUserEntities(Set.of(userEntityFirst, userEntitySecond));
        bankEntitySecond.setUserEntities(Set.of(userEntitySecond, userEntityThird));
        bankEntityThird.setUserEntities(Set.of(userEntitySecond));

        userEntityFirst.setBankEntitySet(Set.of(bankEntityFirst));
        userEntitySecond.setBankEntitySet(Set.of(bankEntityFirst, bankEntitySecond));
        userEntityThird.setBankEntitySet(Set.of(bankEntityThird));
    }
}
