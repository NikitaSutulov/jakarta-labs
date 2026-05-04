package com.team5.jakarta.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class FlywayMigrationConfig {
    @Resource(lookup = "java:app/jdbc/myDataSource")
    private DataSource dataSource;

    @PostConstruct
    public void migrate() {
        if (dataSource == null) {
            System.err.println("Flyway migration skipped: DataSource not found.");
            return;
        }
        System.out.println("Starting Flyway migrations...");
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();
        flyway.migrate();
        System.out.println("Flyway migrations completed successfully!");
    }
}
