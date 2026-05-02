package com.team5.jakarta.config;

import com.team5.jakarta.data.ConnectionManager;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import org.flywaydb.core.Flyway;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class FlywayMigrationConfig {
    @PostConstruct
    public void migrate() {
        System.out.println("Starting Flyway migrations...");
        Flyway flyway = Flyway.configure()
                .dataSource(ConnectionManager.getDs())
                .load();
        flyway.migrate();
        System.out.println("Flyway migrations completed successfully!");
    }
}
