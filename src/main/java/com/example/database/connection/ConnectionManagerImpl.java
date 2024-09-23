package com.example.database.connection;

import com.example.database.migration.FlywayMigration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DataSourceImpl class provides a connection to the database using HikariCP.
 * It initializes a HikariDataSource using the properties specified in the datasource.properties file.
 */
public class ConnectionManagerImpl implements ConnectionManager {

    private static final String DATASOURCE_PROPERTIES_PATH = "/datasource.properties";

    private static HikariDataSource DATA_SOURCE;

    public Connection getConnection() throws SQLException {
        if (DATA_SOURCE == null) {
            initDataSource();
        }
        return DATA_SOURCE.getConnection();
    }

    private static void initDataSource() {
        HikariConfig config = new HikariConfig(DATASOURCE_PROPERTIES_PATH);
        DATA_SOURCE = new HikariDataSource(config);
        new FlywayMigration().migrate(DATA_SOURCE);
    }
}