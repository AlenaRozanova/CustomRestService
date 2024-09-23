package com.example.config;

import com.example.data.dao.*;
import com.example.database.connection.ConnectionManager;
import com.example.database.connection.ConnectionManagerImpl;
import com.example.mapper.*;
import com.example.service.*;
import com.example.servlet.BankServlet;
import com.example.servlet.CountryServlet;
import com.example.servlet.UserBankServlet;
import com.example.servlet.UserServlet;
import com.google.inject.servlet.ServletModule;

public class DependencyInjectionConfig extends ServletModule {

    @Override
    protected void configureServlets() {
        serve("/users/*", "/users").with(UserServlet.class);
        serve("/banks/*", "/banks").with(BankServlet.class);
        serve("/countries/*", "/countries").with(CountryServlet.class);
        serve("/usersbanks/*", "/usersbanks").with(UserBankServlet.class);

        bind(UserDao.class).to(UserDaoImpl.class);

        bind(UserBankDao.class).to(UserBankDaoImpl.class);

        bind(BankDao.class).to(BankDaoImpl.class);

        bind(CountryDao.class).to(CountryDaoImpl.class);

        bind(ConnectionManager.class).to(ConnectionManagerImpl.class);

        bind(UserService.class).to(UserServiceImpl.class);

        bind(BankService.class).to(BankServiceImpl.class);

        bind(CountryService.class).to(CountryServiceImpl.class);

        bind(UserBankService.class).to(UserBankServiceImpl.class);

        bind(UserMapper.class).to(UserMapperImpl.class);

        bind(UserCollectionMapper.class).to(UserCollectionMapperImpl.class);

        bind(BankMapper.class).to(BankMapperImpl.class);

        bind(BankCollectionMapper.class).to(BankCollectionMapperImpl.class);

        bind(CountryMapper.class).to(CountryMapperImpl.class);

        bind(CountryCollectionMapper.class).to(CountryCollectionMapperImpl.class);
    }
}