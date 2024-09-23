package com.example.connection;

import com.google.inject.Inject;
import com.example.database.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerTest implements ConnectionManager {
    private String jdbcUrl;
    private String username;
    private String password;

    @Inject
    public ConnectionManagerTest(final String jdbcUrl, final String username, final String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;

    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
