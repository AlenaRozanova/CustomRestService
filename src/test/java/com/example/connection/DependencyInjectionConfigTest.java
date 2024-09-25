package com.example.connection;

import com.example.data.dao.*;
import com.example.database.connection.ConnectionManager;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;

public class DependencyInjectionConfigTest extends ServletModule {
    private String jdbcUrl;
    private String username;
    private String password;

    public DependencyInjectionConfigTest(final String jdbcUrl, final String username, final String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    protected void configureServlets() {
        bind(UserDao.class).to(UserDaoImpl.class);

        bind(UserBankDao.class).to(UserBankDaoImpl.class);

        bind(BankDao.class).to(BankDaoImpl.class);

        bind(CountryDao.class).to(CountryDaoImpl.class);
    }

    @Provides
    ConnectionManager connectionManager() {
        return new ConnectionManagerTest(jdbcUrl, username, password);
    }
}
