package com.team5.jakarta.config;

import com.team5.jakarta.data.ConnectionManager;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import org.flywaydb.core.Flyway;

import java.util.HashMap;
import java.util.Map;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class FlywayMigrationConfig {
    @PostConstruct
    public void migrate() {
        System.out.println("Starting Flyway migrations...");
        String url = ConnectionManager.getURL();
        Map<String, String> placeholders = new HashMap<>();

        if (url.contains("jdbc:postgresql:")) {
            placeholders.put("sync_categories_id", "SELECT setval(pg_get_serial_sequence('categories', 'id'), MAX(id)) FROM categories");
            placeholders.put("sync_products_id", "SELECT setval(pg_get_serial_sequence('products', 'id'), MAX(id)) FROM products");
        } else {
            placeholders.put("sync_categories_id", "ALTER TABLE categories ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM categories)");
            placeholders.put("sync_products_id", "ALTER TABLE products ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM products)");
        }

        Flyway flyway = Flyway.configure()
                .dataSource(ConnectionManager.getDs())
                .placeholders(placeholders)
                .load();

        flyway.migrate();
        System.out.println("Flyway migrations completed successfully!");
    }
}
