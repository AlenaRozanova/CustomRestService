package com.example;

import com.example.connection.DependencyInjectionConfigTest;
import com.example.utils.SqlUtils;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.sql.Connection;
import java.sql.DriverManager;

public class AbstractDaoTest {
    private static final String DB_SCRIPT_PATH = "db_script.sql";
    @Container
    protected static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    protected static Injector injector;
    private Connection connection;

    @BeforeAll
    public static void setUp() {
        postgresContainer.start();
        injector = Guice.createInjector(
                new DependencyInjectionConfigTest(postgresContainer.getJdbcUrl(),
                        postgresContainer.getUsername(),
                        postgresContainer.getPassword())
        );
    }

    @BeforeEach
    public void connectToDatabase() throws Exception {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        connection = DriverManager.getConnection(jdbcUrl, username, password);
        SqlUtils.executeSql(DB_SCRIPT_PATH, connection);
    }

    @AfterEach
    public void closeConnection() throws Exception {
        connection.close();
    }
}
