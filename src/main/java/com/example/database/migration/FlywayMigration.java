package com.example.database.migration;

import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

public class FlywayMigration {
    public void migrate(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db.migration")
                .load();
        flyway.migrate();
    }
}
